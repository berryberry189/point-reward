package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDate;
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
        pointRewardRepository.findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtAsc(
            today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  @DisplayName("날짜별 포인트 지급 테스트")
  void findByMemberIdAndRewardedAtGreaterThanEqualAndRewardedAtLessThan(){
    String memberId = "member_1";
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .rewardedPoint(100)
        .build());

    PointReward pointReward = pointRewardRepository.findByMemberIdAndRewardedAtGreaterThanEqualAndRewardedAtLessThan(memberId,
        today.atStartOfDay(), today.plusDays(1).atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트 정렬 시간 오름차순")
  void findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtAsc() {
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .rewardedPoint(100)
        .build());
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_2")
        .rewardedPoint(100)
        .build());

    List<PointReward> pointRewardList =
        pointRewardRepository.findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtAsc(
            today.atStartOfDay(), today.plusDays(1).atStartOfDay());

    assertEquals(2, pointRewardList.size());
    assertEquals("member_1", pointRewardList.get(0).getMemberId());
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트 정렬 시간 내림차순")
  void findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtDesc() {
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .rewardedPoint(100)
        .build());
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_2")
        .rewardedPoint(100)
        .build());

    List<PointReward> pointRewardList =
        pointRewardRepository.findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtDesc(
            today.atStartOfDay(), today.plusDays(1).atStartOfDay());

    assertEquals(2, pointRewardList.size());
    assertEquals("member_2", pointRewardList.get(0).getMemberId());
  }

}