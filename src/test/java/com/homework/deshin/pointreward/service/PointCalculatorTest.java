package com.homework.deshin.pointreward.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = PointCalculator.class)
class PointCalculatorTest {

  @Autowired
  PointCalculator pointCalculator;

  @MockBean
  PointRewardRepository pointRewardRepository;

  private LocalDate today = LocalDate.now();

  @Test
  void 전날에_출석한_이력있으면_전날포인트_더하기_100포인트_제공() {
    String memberId = "member_1";

    given(pointRewardRepository.findByMemberIdAndRewardAtBetween(
        memberId, today.minusDays(1).atStartOfDay(), LocalDateTime.of(today.minusDays(1), LocalTime.of(23,59,59))))
        .willReturn(Optional.of(PointReward.builder()
            .point(100)
            .memberId(memberId)
            .rewardAt(today.minusDays(1).atStartOfDay())
            .build()));

    int point = pointCalculator.calculate(memberId, today);

    assertEquals(200, point);
  }

}