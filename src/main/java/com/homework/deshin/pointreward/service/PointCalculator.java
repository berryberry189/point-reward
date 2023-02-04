package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointCalculator {

  private final PointRewardRepository pointRewardRepository;

  public int calculate(String memberId, LocalDate today) {
    int point = 100;
    LocalDateTime end = LocalDateTime.of(today, LocalTime.of(23,59,59));
    Optional<PointReward> yesterdayPointRewardOpt =
        pointRewardRepository.findByMemberIdAndRewardAtBetween(
            memberId, today.minusDays(1).atStartOfDay(), end);

    if(yesterdayPointRewardOpt.isPresent()) {
      int yesterdayPoint = yesterdayPointRewardOpt.get().getPoint();
      if(yesterdayPoint < 1000) {
        point += yesterdayPointRewardOpt.get().getPoint();
      }
    }

    return point;
  }

}
