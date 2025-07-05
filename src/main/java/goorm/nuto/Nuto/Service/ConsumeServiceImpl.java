package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CardDto;
import goorm.nuto.Nuto.Dto.ConsumeRequestDto;
import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Exception.DuplicateCardNumberException;
import goorm.nuto.Nuto.Exception.NotFoundCardException;
import goorm.nuto.Nuto.Exception.NotFoundCategoryException;
import goorm.nuto.Nuto.Exception.NotFoundMemberException;
import goorm.nuto.Nuto.Repository.CardRepository;
import goorm.nuto.Nuto.Repository.CategoryRepository;
import goorm.nuto.Nuto.Repository.ConsumeRepository;
import goorm.nuto.Nuto.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsumeServiceImpl implements ConsumeService {
    private final ConsumeRepository consumeRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;


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
}
