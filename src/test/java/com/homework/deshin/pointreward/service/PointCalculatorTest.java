package com.homework.deshin.pointreward.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PointCalculatorTest {

  PointCalculator pointCalculator;

  @Mock
  PointRewardRepository pointRewardRepository;

  private LocalDate today = LocalDate.now();

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.pointCalculator = new PointCalculator(pointRewardRepository);
  }

  @Test
  void 전날에_출석한_이력있으면_전날포인트_더하기_100포인트_제공() {
    String memberId = "member_1";

    given(pointRewardRepository.findByMemberIdAndRewardedAtGreaterThanEqualAndRewardedAtLessThan(
        memberId, today.minusDays(1).atStartOfDay(), today.atStartOfDay()))
        .willReturn(Optional.of(PointReward.builder()
            .rewardedPoint(100)
            .memberId(memberId)
            .rewardedAt(today.minusDays(1).atStartOfDay())
            .build()));

    int point = pointCalculator.calculate(memberId, today);

    assertEquals(200, point);
  }

}