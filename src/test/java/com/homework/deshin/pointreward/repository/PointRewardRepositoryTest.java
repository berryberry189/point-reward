package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointRewardRepositoryTest {

  @Autowired PointRewardRepository pointRewardRepository;

  @BeforeEach
  void PointReward_당일데이터삭제() {
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqual(LocalDate.now().atStartOfDay());
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
    String memberId = "member_1";

    PointReward pointReward = pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqualAndPayAtLessThan(memberId,
        LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

}