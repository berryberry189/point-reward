package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.domain.RewardLimit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Repository Layer Unit Test
 */
@SpringBootTest
class PointRewardRepositoryTest {

  @Autowired PointRewardRepository pointRewardRepository;

  @Autowired
  RewardLimitRepository rewardLimitRepository;

  @BeforeEach
  void initRewardLimit() {
    Optional<RewardLimit> rewardLimitOpt = rewardLimitRepository.findByPayDate(LocalDate.now());
    if (rewardLimitOpt.isPresent()) {
      rewardLimitRepository.delete(rewardLimitOpt.get());
    }
    rewardLimitRepository.save(rewardLimitRepository.save(RewardLimit.builder()
        .payDate(LocalDate.now())
        .limitCount(10)
        .build()));
  }

  @BeforeEach
  void PointReward_당일데이터삭제() {
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqualOrderByPayAtAsc(LocalDate.now().atStartOfDay());
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  void findByMemberIdAndPayAtGreaterThanEqual_테스트(){
    String memberId = "member_1";
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .point(100)
        .payAt(LocalDateTime.now())
        .build());

    PointReward pointReward = pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqual(memberId,
        LocalDate.now().atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

  @Test
  void findByMemberIdAndPayAtGreaterThanEqualAndPayAtLessThan_테스트(){
    // db insert 용 데이터로 이전 날짜 데이터 구성
    String memberId = "member_1";
    PointReward pointReward = pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqualAndPayAtLessThan(memberId,
        LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

}