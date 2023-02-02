package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.domain.RewardLimit;
import com.homework.deshin.pointreward.repository.RewardLimitRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticLockRewardLimitService {


  private final RewardLimitRepository rewardLimitRepository;

  @Transactional
  public void decrease(LocalDate today) {
    RewardLimit rewardLimit = rewardLimitRepository.findByPayDateWithPessimisticLock(today);
    // 재고감소
    rewardLimit.decrease();

    rewardLimitRepository.saveAndFlush(rewardLimit);
  }


}
