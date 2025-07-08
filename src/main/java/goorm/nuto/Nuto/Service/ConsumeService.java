package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Entity.Member;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ConsumeService {
    void saveReceipt(Long memberId, ConsumeRequestDto dto);
    // 전체 데이터 조회
    List<RecordResponseDto> getReceipts(Member member, CategoryType categoryType);
    // 기록 삭제
    void deleteReceipt(Member member, RecordRemoveRequestDto removeRequest);
    // 소비 기록 시 카드 목록 조회
    List<CardNameResponseDto> getCardNames(Member member);
    // 월별 데이터 조회
    List<RecordResponseDto> getMonthlyReceipts(Member member, YearMonth date, CategoryType categoryType);
    // 기록 수정
    void updateReceipt(Member member, RecordRequestDto dto);
}
