package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.dto.PointRewardListResponse;
import com.homework.deshin.pointreward.dto.PointRewardResponse;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import com.homework.deshin.pointreward.repository.RedisRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private final LocalDate today = LocalDate.now();

  @Transactional
  public PointRewardResponse payPointReward(PayPointRequest request) {
    final long now = System.currentTimeMillis();
    String memberId = request.getMemberId();
    redisRepository.addIfAbsent(today.toString(), memberId, (int) now);

    long rank = redisRepository.getRank(today.toString(), memberId);
    if(rank > 9) throw new IllegalArgumentException("선착순이 종료되었습니다.");

    duplicateCheck(memberId);

    int point = pointCalculator.calculate(memberId, today);

    PointReward savedPointReward = pointRewardRepository.save(PointReward.builder()
        .memberId(memberId)
        .rewardAt(LocalDateTime.now())
        .point(point)
        .build());

    return new PointRewardResponse(savedPointReward);
  }


  private void duplicateCheck(String memberId) {
    Optional<PointReward> pointRewardOptional =
        pointRewardRepository.findByMemberIdAndRewardAtGreaterThanEqual(memberId, today.atStartOfDay());
    if (pointRewardOptional.isPresent()) {
      throw new IllegalArgumentException("이미 참여완료 되었습니다.");
    }
  }


  @Transactional(readOnly = true)
  public PointRewardListResponse getPointRewardList(LocalDate rewardDate, PointRewardSort sort) {
    LocalDateTime start = rewardDate.atStartOfDay();
    LocalDateTime end = LocalDateTime.of(rewardDate, LocalTime.of(23,59,59));
    List<PointReward> pointRewardList;
    if(PointRewardSort.ASC.equals(sort)) {
      pointRewardList = pointRewardRepository.findByRewardAtBetweenOrderByRewardAtAsc(start, end);
    }
    else {
      pointRewardList = pointRewardRepository.findByRewardAtBetweenOrderByRewardAtDesc(start, end);
    }
    List<PointRewardResponse> pointRewardResponseList = pointRewardList.stream()
        .map(PointRewardResponse::new)
        .collect(Collectors.toList());

    return new PointRewardListResponse(pointRewardResponseList);
  }

  @Transactional(readOnly = true)
  public PointRewardResponse getPointReward(Long pointRewardId) {
    PointReward pointReward = getPointRewardEntity(pointRewardId);
    return new PointRewardResponse(pointReward);
  }

  private PointReward getPointRewardEntity(Long pointRewardId) {
    return pointRewardRepository.findById(pointRewardId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포인트 지급 내역 ID=" + pointRewardId));
  }


}
