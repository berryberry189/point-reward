package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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


  @BeforeEach
  void PointReward_당일데이터삭제() {
    LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
    List<PointReward> pointRewardList = pointRewardRepository.findByRewardAtBetweenOrderByRewardAtAsc(LocalDate.now().atStartOfDay(), end);
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트")
  void findByMemberIdAndRewardAtGreaterThanEqual(){
    String memberId = "member_1";
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .point(100)
        .rewardAt(LocalDateTime.now())
        .build());

    PointReward pointReward = pointRewardRepository.findByMemberIdAndRewardAtGreaterThanEqual(memberId,
        LocalDate.now().atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트")
  void findByMemberIdAndRewardAtBetween(){
    String memberId = "member_1";
    LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
    PointReward pointReward = pointRewardRepository.findByMemberIdAndRewardAtBetween(memberId,
        LocalDate.now().minusDays(1).atStartOfDay(), end).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

}