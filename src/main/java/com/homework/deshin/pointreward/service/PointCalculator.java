package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointCalculator {

  private final PointRewardRepository pointRewardRepository;

  @Transactional(readOnly = true)
  public int calculate(String memberId, LocalDate today) {
    int point = 100;
    LocalDateTime start = today.minusDays(1).atStartOfDay();
    LocalDateTime end = today.atStartOfDay();
    Optional<PointReward> yesterdayPointRewardOpt =
        pointRewardRepository.findByMemberIdAndRewardedAtGreaterThanEqualAndRewardedAtLessThan(memberId, start, end);

    if(yesterdayPointRewardOpt.isPresent()) {
      int yesterdayPoint = yesterdayPointRewardOpt.get().getRewardedPoint();
      if(yesterdayPoint < 1000) {
        point += yesterdayPointRewardOpt.get().getRewardedPoint();
      }
    }

    return point;
  }

}
