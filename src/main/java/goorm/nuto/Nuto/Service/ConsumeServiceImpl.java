package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Exception.*;
import goorm.nuto.Nuto.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumeServiceImpl implements ConsumeService {
    private final ConsumeRepository consumeRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final ReceiptRepository receiptRepository;
    private final CloudinaryService cloudinaryService;


    @Override
    public void saveReceipt(Long memberId, ConsumeRequestDto dto) {
        // 1) memberId 로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(NotFoundCardException::new);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(NotFoundCategoryException::new);

        // 2) Consume 엔티티 생성 & 저장
        Consume consume = Consume.builder()
                .name(dto.getName())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .year(dto.getDate().getYear())
                .month(dto.getDate().getMonthValue())
                .day(dto.getDate().getDayOfMonth())
                .weekOfYear(dto.getDate().get(java.time.temporal.WeekFields.ISO.weekOfYear()))
                .card(card)
                .category(category)
                .member(member)
                .build();

        consumeRepository.save(consume);

    }

    // 전체 데이터 조회
    @Override
    public List<RecordResponseDto> getReceipts(Member member, CategoryType categoryType) {
        List<Receipt> receipts = receiptRepository.findByMemberAndCategory_Type(member, categoryType);

        return receipts.stream()
                .map(receipt -> RecordResponseDto.builder()
                        .id(receipt.getId())
                        .shopName(receipt.getShop_name())
                        .fileName(receipt.getName())
                        .amount(receipt.getPrice())
                        .date(receipt.getDate())
                        .card(CardDto.builder()
                                .id(receipt.getCard().getId())
                                .cardNumber(receipt.getCard().getCardNumber())
                                .cardType(receipt.getCard().getCardType().name())
                                .build())
                        .category(CategoryDto.builder()
                                .id(receipt.getCategory().getId())
                                .name(receipt.getCategory().getName())
                                .type(receipt.getCategory().getType().name())
                                .build())
                        .image(receipt.getImageUrl())
                        .build())
                .toList();
    }

    // 기록 삭제
    @Override
    public void deleteReceipt(Member member, RecordRemoveRequestDto dto) {
        Receipt receipt = receiptRepository.findByMemberAndId(member, dto.getId())
                .orElseThrow(NotFoundReceiptException::new);
        try {
            cloudinaryService.deleteImage(receipt.getImageUrl());
        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제 실패", e);
        }

        receiptRepository.delete(receipt);
    }

    // 소비 기록 시 카드 목록 조회
    @Override
    public List<CardNameResponseDto> getCardNames(Member member) {
        Optional<List<Card>> optionalCards = cardRepository.findByMember(member);

        return optionalCards
                .orElseGet(List::of)
                .stream()
                .map(card -> CardNameResponseDto.builder()
                        .id(card.getId())
                        .name(card.getName())
                        .build())
                .toList();
    }

    // 월별 데이터 조회
    @Override
    public List<RecordResponseDto> getMonthlyReceipts(Member member, YearMonth date, CategoryType categoryType) {
        LocalDate start = date.atDay(1);
        LocalDate end = date.atEndOfMonth();

        List<Receipt> receipts = receiptRepository.findByMemberAndCategory_TypeAndDateBetween(member, categoryType, start, end);

        // DTO 변환
        return receipts.stream()
                .map(receipt -> RecordResponseDto.builder()
                        .id(receipt.getId())
                        .shopName(receipt.getShop_name())
                        .fileName(receipt.getName())
                        .amount(receipt.getPrice())
                        .date(receipt.getDate())
                        .card(CardDto.builder()
                                .id(receipt.getCard().getId())
                                .cardNumber(receipt.getCard().getCardNumber())
                                .cardType(receipt.getCard().getCardType().name())
                                .build())
                        .category(CategoryDto.builder()
                                .id(receipt.getCategory().getId())
                                .name(receipt.getCategory().getName())
                                .type(receipt.getCategory().getType().name())
                                .build())
                        .image(receipt.getImageUrl())
                        .build())
                .toList();
    }

    // 기록 수정
    @Override
    public void updateReceipt(Member member, RecordRequestDto dto) {
        Receipt receipt = receiptRepository.findByMemberAndId(member, dto.getId())
                .orElseThrow(NotFoundReceiptException::new);

        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(NotFoundCardException::new);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(NotFoundCategoryException::new);

        MultipartFile newImage = dto.getImage();
        if (newImage != null && !newImage.isEmpty()) {
            try {
                if (receipt.getImageUrl() != null && !receipt.getImageUrl().isEmpty()) {
                    cloudinaryService.deleteImage(receipt.getImageUrl());
                }

                String newImageUrl = cloudinaryService.uploadImage(newImage);
                receipt.setImageUrl(newImageUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 수정 실패", e);
            }
        }
        receipt.setName(dto.getFileName());
        receipt.setShop_name(dto.getName());
        receipt.setPrice(dto.getAmount());
        receipt.setDate(dto.getDate());
        receipt.setCard(card);
        receipt.setCategory(category);
    }
    // 전체 소비내역 조회
    @Override
    public PageResponseDto<ConsumeListResponseDto> getAllConsumeList(Member member, Pageable pageable) {
        Page<Consume> consumePage = consumeRepository.findAllByMember(member, pageable);

        Page<ConsumeListResponseDto> mappedPage = consumePage.map(consume -> ConsumeListResponseDto.builder()
                .name(consume.getName())
                .amount(consume.getAmount())
                .cardName(consume.getCard().getName())
                .date(consume.getDate())
                .categoryName(consume.getCategory().getName())
                .build());

        return PageResponseDto.of(mappedPage);
    }

    // 카테고리별 소비내역 조회
    @Override
    public PageResponseDto<ConsumeListResponseDto> getConsumeListByCategory(Member member, CategoryDto categoryDto, Pageable pageable) {
        Page<Consume> page = consumeRepository.findByMemberAndCategoryName(member, categoryDto.getName(), pageable);

        Page<ConsumeListResponseDto> mappedPage = page.map(consume -> ConsumeListResponseDto.builder()
                .name(consume.getName())
                .amount(consume.getAmount())
                .cardName(consume.getCard().getName())
                .date(consume.getDate())
                .categoryName(consume.getCategory().getName())
                .build());

        return PageResponseDto.of(mappedPage);
    }

    // 월별 소비내역 조회
    @Override
    public PageResponseDto<ConsumeListResponseDto> getConsumeListByMonth(Member member, YearMonth date, Pageable pageable) {
        int year = date.getYear();
        int month = date.getMonthValue();

        Page<Consume> page = consumeRepository.findByMemberAndYearAndMonth(member, year, month, pageable);

        Page<ConsumeListResponseDto> mappedPage = page.map(consume -> ConsumeListResponseDto.builder()
                .name(consume.getName())
                .amount(consume.getAmount())
                .cardName(consume.getCard().getName())
                .date(consume.getDate())
                .categoryName(consume.getCategory().getName())
                .build());

        return PageResponseDto.of(mappedPage);
    }

    // 카드별 소비내역 조회
    @Override
    public PageResponseDto<ConsumeListResponseDto> getConsumeListByCards(Member member, CardRequestDto cardRequestDto, Pageable pageable) {
        Card card = cardRepository.findByMemberAndCardNumber(member, cardRequestDto.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));

        Page<Consume> page = consumeRepository.findByMemberAndCard(member, card, pageable);

        Page<ConsumeListResponseDto> mappedPage = page.map(consume -> ConsumeListResponseDto.builder()
                .name(consume.getName())
                .amount(consume.getAmount())
                .cardName(consume.getCard().getName())
                .date(consume.getDate())
                .categoryName(consume.getCategory().getName())
                .build());

        return PageResponseDto.of(mappedPage);
    }
    // 카테고리 조회
    @Override
    public List<CategoryDto> getCategory() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType().name())
                        .build())
                .toList();
    }
    // 보유 카드 조회
    @Override
    public List<CardResponseDto> getCards(Member member) {
        List<Card> cards = cardRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("보유한 카드가 없습니다."));

        return cards.stream()
                .map(CardResponseDto::new)
                .toList();
    }
}
