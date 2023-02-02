package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.domain.PointRewardDto;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
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
  public PointRewardDto payPointReward(PayPointRequest request) {

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

    PointReward savedPointReward = pointRewardRepository.save(PointReward.builder()
        .memberId(request.getMemberId())
        .payAt(LocalDateTime.now())
        .point(point)
        .build());

    return new PointRewardDto(savedPointReward);
  }

  @Transactional(readOnly = true)
  @Override
  public List<PointRewardDto> getPointRewardList(LocalDate payDate, PointRewardSort sort) {
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqualOrderByPayAtAsc(payDate.atStartOfDay());

    Stream<PointRewardDto> pointRewardDtoStream = pointRewardList.stream()
        .map(PointRewardDto::new);
    if(sort.equals(PointRewardSort.DESC)) {
      pointRewardDtoStream = pointRewardDtoStream
          .sorted(Comparator.comparing(PointRewardDto::getPayAt).reversed());
    }
    return pointRewardDtoStream.collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public PointRewardDto getPointReward(Long pointRewardId) {
    PointReward pointReward = getPointRewardEntity(pointRewardId);
    return new PointRewardDto(pointReward);
  }

  private PointReward getPointRewardEntity(Long pointRewardId) {
    return pointRewardRepository.findById(pointRewardId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포인트 지급 내역 ID=" + pointRewardId));
  }


}
