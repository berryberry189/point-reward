package com.homework.deshin.pointreward.scheduler;

import com.homework.deshin.pointreward.domain.RewardLimit;
import com.homework.deshin.pointreward.repository.RewardLimitRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RewardLimitScheduler {

  private final RewardLimitRepository rewardLimitRepository;

  @Scheduled(cron = "0 0 0 * * *")
  private void rewardLimitScheduler() {
    rewardLimitRepository.save(RewardLimit.builder()
        .payDate(LocalDate.now().plusDays(1))
        .limitCount(10)
        .build());
  }
}
