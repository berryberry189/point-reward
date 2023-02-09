package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.dto.PointRewardDto;
import com.homework.deshin.pointreward.dto.PointRewardListResponse;
import com.homework.deshin.pointreward.dto.PointRewardRequest;
import com.homework.deshin.pointreward.dto.PointRewardResponse;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import com.homework.deshin.pointreward.repository.RedisRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRewardService {

  private final PointRewardRepository pointRewardRepository;
  private final RedisRepository redisRepository;
  private final PointCalculator pointCalculator;
  private LocalDate today = LocalDate.now();

  @Transactional
  public PointRewardDto payPointReward(PointRewardRequest request) {
    final long now = System.currentTimeMillis();
    String memberId = request.getMemberId();
    redisRepository.addIfAbsent(today.toString(), memberId, (int) now);

    long rank = redisRepository.getRank(today.toString(), memberId);
    if (rank > 9) {
      throw new IllegalArgumentException("선착순이 종료되었습니다.");
    }

    duplicateCheck(memberId);

    int rewardedPoint = pointCalculator.calculate(memberId, today);

    PointReward savedPointReward = pointRewardRepository.save(PointReward.builder()
        .memberId(memberId)
        .rewardedAt(LocalDateTime.now())
        .rewardedPoint(rewardedPoint)
        .build());

    return new PointRewardDto(savedPointReward);
  }


  private void duplicateCheck(String memberId) {
    Optional<PointReward> pointRewardOptional =
        pointRewardRepository.findByMemberIdAndRewardedAtGreaterThanEqualAndRewardedAtLessThan(
            memberId, today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    if (pointRewardOptional.isPresent()) {
      throw new IllegalArgumentException("이미 참여완료 되었습니다.");
    }
  }


  @Transactional(readOnly = true)
  public PointRewardListResponse getPointRewardList(LocalDate rewardDate, PointRewardSort sort) {
    LocalDateTime start = rewardDate.atStartOfDay();
    LocalDateTime end = rewardDate.plusDays(1).atStartOfDay();
    List<PointReward> pointRewardList;
    if (PointRewardSort.ASC.equals(sort)) {
      pointRewardList = pointRewardRepository.findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtAsc(start, end);
    } else {
      pointRewardList = pointRewardRepository.findAllByRewardedAtGreaterThanEqualAndRewardedAtLessThanOrderByRewardedAtDesc(start, end);
    }
    List<PointRewardDto> pointRewardDtoList = pointRewardList.stream()
        .map(PointRewardDto::new)
        .collect(Collectors.toList());

    return new PointRewardListResponse(pointRewardDtoList);
  }

  @Transactional(readOnly = true)
  public PointRewardResponse getPointReward(Long pointRewardId) {
    PointReward pointReward = getPointRewardEntity(pointRewardId);

    LocalDate rewardDate = pointReward.getRewardedAt().toLocalDate();
    Long prePointRewardId = null;
    Optional<PointReward> pointRewardOptional =
        pointRewardRepository.findByMemberIdAndRewardedAtGreaterThanEqualAndRewardedAtLessThan(
            pointReward.getMemberId(), rewardDate.minusDays(1).atStartOfDay(), rewardDate.atStartOfDay());
    if (pointRewardOptional.isPresent()) {
      prePointRewardId = pointRewardOptional.get().getId();
    }

    return new PointRewardResponse(pointReward, prePointRewardId);
  }

  private PointReward getPointRewardEntity(Long pointRewardId) {
    return pointRewardRepository.findById(pointRewardId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포인트 지급 내역 ID=" + pointRewardId));
  }


}
