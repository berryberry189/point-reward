package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Repository Layer Unit Test
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointRewardRepositoryTest {

  @Autowired PointRewardRepository pointRewardRepository;

  private LocalDate today = LocalDate.now();


  @AfterEach
  void PointReward_당일데이터삭제() {
    List<PointReward> pointRewardList =
        pointRewardRepository.findAllByRewardedAtBetweenOrderByRewardedAtAsc(getStartTime(today), getEndTime(today));
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  @DisplayName("날짜별 포인트 지급 테스트")
  void findByMemberIdAndRewardedAtBetween(){
    String memberId = "member_1";
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .rewardedPoint(100)
        .build());

    PointReward pointReward = pointRewardRepository.findByMemberIdAndRewardedAtBetween(memberId,
        getStartTime(today), getEndTime(today)).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트 정렬 시간 오름차순")
  void findAllByRewardedAtBetweenOrderByRewardedAtAsc() {
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .rewardedPoint(100)
        .build());
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_2")
        .rewardedPoint(100)
        .build());

    List<PointReward> pointRewardList =
        pointRewardRepository.findAllByRewardedAtBetweenOrderByRewardedAtAsc(getStartTime(today), getEndTime(today));

    assertEquals(2, pointRewardList.size());
    assertEquals("member_1", pointRewardList.get(0).getMemberId());
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트 정렬 시간 내림차순")
  void findAllByRewardedAtBetweenOrderByRewardedAtDesc() {
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .rewardedPoint(100)
        .build());
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_2")
        .rewardedPoint(100)
        .build());

    List<PointReward> pointRewardList =
        pointRewardRepository.findAllByRewardedAtBetweenOrderByRewardedAtDesc(getStartTime(today), getEndTime(today));

    assertEquals(2, pointRewardList.size());
    assertEquals("member_2", pointRewardList.get(0).getMemberId());
  }

  private LocalDateTime getStartTime(LocalDate date) {
    return date.atStartOfDay();
  }
  private LocalDateTime getEndTime(LocalDate date) {
    return LocalDateTime.of(date, LocalTime.of(23,59,59));
  }

}