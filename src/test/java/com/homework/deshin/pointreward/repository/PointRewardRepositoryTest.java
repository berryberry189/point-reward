package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointRewardRepositoryTest {

  @Autowired PointRewardRepository pointRewardRepository;

  @Test
  void findByMemberIdAndPayAtGreaterThanEqual_테스트(){
    String memberId = "user01";
    pointRewardRepository.save(PointReward.builder()
        .memberId("user01")
        .point(100)
        .payAt(LocalDateTime.now())
        .build());

    PointReward pointReward = pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqual(memberId,
        LocalDate.now().atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

}