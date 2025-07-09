package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Exception.*;
import goorm.nuto.Nuto.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final ConsumeRepository consumeRepository;
    private final IncomeRepository incomeRepository;


    @Override
    public void saveReceipt(Long memberId, ReceiptRequestDto dto) {
        // 1) memberId 로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(NotFoundCardException::new);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(NotFoundCategoryException::new);

        // 2) Receipt 엔티티 생성 & 저장
        Receipt receipt = Receipt.builder()
                .name(dto.getName())
                .shop_name(dto.getShop_name())
                .date(dto.getDate())
                .price(dto.getPrice())
                .card(card)
                .category(category)
                .member(member)
                .imageUrl(dto.getImageUrl())
                .build();

        receiptRepository.save(receipt);

        if(category.getType().equals(CategoryType.EXPENSE)) {
            Consume consume = Consume.builder()
                    .name(dto.getName())
                    .amount(dto.getPrice())
                    .date(dto.getDate())
                    .year(dto.getDate().getYear())
                    .month(dto.getDate().getMonthValue())
                    .day(dto.getDate().getDayOfMonth())
                    .weekOfYear(dto.getDate().get(java.time.temporal.WeekFields.ISO.weekOfYear()))
                    .card(card)
                    .category(category)
                    .member(member)
                    .receipt(receipt)
                    .build();
            consumeRepository.save(consume);
        }else{
            Income income = Income.builder()
                    .category(category)
                    .amount((double)dto.getPrice())
                    .date(dto.getDate())
                    .card(card)
                    .member(member)
                    .receipt(receipt)
                    .build();
            incomeRepository.save(income);
        }

    }


    @Override
    public ReceiptResponseDto getReceipt(Long memberId, Long receiptId) {

        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(NotFoundReceiptException::new);

        if(!receipt.getMember().getId().equals(memberId)) {
            throw new NotAuthorizedReceiptAccessException("해당 사용자의 영수증 아닙니다.");
        }

        ReceiptResponseDto dto = ReceiptResponseDto.builder()
                .name(receipt.getName())
                .shop_name(receipt.getShop_name())
                .date(receipt.getDate())
                .price(receipt.getPrice())
                .categoryName(receipt.getCategory().getName())
                .build();

        return dto;

    }

    @Override
    public Page<ReceiptResponseDto> getReceiptListPage(int page, int size, Long memberId) {
        Pageable pageable = PageRequest.of(page, size);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Page<Receipt> receipts = receiptRepository.findReceiptsByMemberOrderByIdDesc(member, pageable);

        return receipts.map(ReceiptResponseDto::new);
    }

    @Override
    public Page<MonthlyReceiptDto> getMonthlyReceiptPage(int page, int size, Long memberId) {
        Pageable pageable = PageRequest.of(page, size);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        return receiptRepository.findMonthlyReceiptByMemberId(member, pageable);
    }

    @Override
    public Page<ReceiptResponseDto> getConsumeListPage(int page, int size, Long memberId, int year, int month) {
        Pageable pageable = PageRequest.of(page, size);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Page<Receipt> receipts = receiptRepository.findExpenseReceiptsByMemberAndYearMonth(member, year, month, pageable);

        return receipts.map(ReceiptResponseDto::new);
    }

    @Override
    public Page<ReceiptResponseDto> getIncomeListPage(int page, int size, Long memberId) {
        Pageable pageable = PageRequest.of(page, size);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Page<Receipt> receipts = receiptRepository.findIncomeReceiptsByMember(member, pageable);

        return receipts.map(ReceiptResponseDto::new);
    }

    @Override
    public void modifyReceipt(Long memberId, ReceiptRequestDto dto) {
        Receipt receipt = receiptRepository.findById(dto.getId())
                .orElseThrow(NotFoundReceiptException::new);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        CategoryType originalType = receipt.getCategory().getType();

        if (!receipt.getMember().getId().equals(memberId)) {
            throw new NotAuthorizedReceiptAccessException("해당 사용자의 영수증 아닙니다.");
        }

        // 1. 영수증 수정
        if (dto.getName() != null) receipt.setName(dto.getName());
        if (dto.getShop_name() != null) receipt.setShop_name(dto.getShop_name());
        if (dto.getPrice() != null) receipt.setPrice(dto.getPrice());
        if (dto.getDate() != null) receipt.setDate(dto.getDate());
        if (dto.getCardId() != null) {
            Card card = cardRepository.findById(dto.getCardId())
                    .orElseThrow(NotFoundCardException::new);
            receipt.setCard(card);
        }
        if (dto.getCategoryId() != null) {
            Category newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(NotFoundCategoryException::new);
            receipt.setCategory(newCategory);
        }


        // 2. CategoryType 수정 -> 새 CategoryType / 수정X -> 기존 CategoryType
        CategoryType newType = (dto.getCategoryId() != null)
                ? categoryRepository.findById(dto.getCategoryId()).orElseThrow(NotFoundCategoryException::new).getType()
                : originalType;

        receiptRepository.save(receipt);

        // 3. Category 변경 -> 기존 수입/지출 삭제 + 새로 생성
        if (!originalType.equals(newType)) {
            // 소비 → 수입으로 변경
            if (originalType == CategoryType.EXPENSE) {
                consumeRepository.deleteByReceiptId(receipt.getId());

                Income income = Income.builder()
                        .receipt(receipt)
                        .member(member)
                        .category(receipt.getCategory())
                        .date(receipt.getDate())
                        .amount((double) receipt.getPrice())
                        .card(receipt.getCard())
                        .build();
                incomeRepository.save(income);
            }
            // 수입 → 소비로 변경
            else {
                incomeRepository.deleteByReceiptId(receipt.getId());

                Consume consume = Consume.builder()
                        .receipt(receipt)
                        .member(member)
                        .category(receipt.getCategory())
                        .date(receipt.getDate())
                        .name(receipt.getName())
                        .amount(receipt.getPrice())
                        .year(receipt.getDate().getYear())
                        .month(receipt.getDate().getMonthValue())
                        .day(receipt.getDate().getDayOfMonth())
                        .weekOfYear(receipt.getDate().get(java.time.temporal.WeekFields.ISO.weekOfYear()))
                        .card(receipt.getCard())
                        .build();
                consumeRepository.save(consume);
            }
        }
        // 4. CategoryType 변경X -> 기존 consume/income 업데이트
        else {
            if (originalType == CategoryType.EXPENSE) {
                Consume consume = consumeRepository.findConsumeByReceiptId(receipt.getId())
                        .orElseThrow(NotFoundConsumeException::new);

                consume.setName(receipt.getName());
                consume.setAmount(receipt.getPrice());
                consume.setDate(receipt.getDate());
                consume.setYear(receipt.getDate().getYear());
                consume.setMonth(receipt.getDate().getMonthValue());
                consume.setDay(receipt.getDate().getDayOfMonth());
                consume.setWeekOfYear(receipt.getDate().get(java.time.temporal.WeekFields.ISO.weekOfYear()));
                consume.setCard(receipt.getCard());
                consume.setCategory(receipt.getCategory());

                consumeRepository.save(consume);
            } else {
                Income income = incomeRepository.findIncomeByReceiptId(receipt.getId())
                        .orElseThrow(NotFoundIncomeException::new);

                income.setAmount((double) receipt.getPrice());
                income.setDate(receipt.getDate());
                income.setCard(receipt.getCard());
                income.setCategory(receipt.getCategory());

                incomeRepository.save(income);
            }
        }
    }

    @Override
    public void deleteReceipts(Long memberId,List<Long> receiptIds) {
        for (Long receiptId : receiptIds){
            Receipt receipt = receiptRepository.findById(receiptId)
                    .orElseThrow(NotFoundReceiptException::new);

            Category category = receipt.getCategory();

            if (!receipt.getMember().getId().equals(memberId)) {
                throw new NotAuthorizedReceiptAccessException("해당 사용자의 영수증 아닙니다.");
            }

            if(category.getType().equals(CategoryType.EXPENSE)) {
                consumeRepository.deleteByReceiptId(receiptId);
            }else{
                incomeRepository.deleteByReceiptId(receiptId);
            }
            receiptRepository.delete(receipt);
        }
    }

}
