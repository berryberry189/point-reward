package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.repository.RedisRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRewardServiceImpl implements PointRewardService {

  private final RedisRepository redisRepository;
  private static final int limit = 10;
  private final String today = LocalDate.now().toString();

  @Override
  public void addWaitQueue(PayPointRequest request){
    final long now = System.currentTimeMillis();
    String memberId = request.getMemberId();
    redisRepository.addIfAbsent(today, memberId, (int) now);
    log.info("대기열에 추가 - {} ({}초)", memberId, now);

  }


}
