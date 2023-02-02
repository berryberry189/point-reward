package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRewardServiceImpl implements PointRewardService {

  private final PointRewardRepository pointRewardRepository;
  private final PessimisticLockRewardLimitService pessimisticLockRewardLimitService;
  private final LocalDate today = LocalDate.now();


  @Transactional
  @Override
  public void payPointReward(PayPointRequest request) {

    Optional<PointReward> pointRewardOptional =
        pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqual(request.getMemberId(), today.atStartOfDay());
    if (pointRewardOptional.isPresent()) {
      throw new IllegalArgumentException("이미 참여완료 되었습니다.");
    }

    pessimisticLockRewardLimitService.decrease(today);

    int point = 100;
    Optional<PointReward> yesterdayPointRewardOpt =
        pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqualAndPayAtLessThan(
            request.getMemberId(), today.minusDays(1).atStartOfDay(), today.atStartOfDay());

    if(yesterdayPointRewardOpt.isPresent()) {
      int yesterdayPoint = yesterdayPointRewardOpt.get().getPoint();
      if(yesterdayPoint < 1000) {
        point += yesterdayPointRewardOpt.get().getPoint();
      }
    }

    pointRewardRepository.save(PointReward.builder()
        .memberId(request.getMemberId())
        .payAt(LocalDateTime.now())
        .point(point)
        .build());
  }


}
