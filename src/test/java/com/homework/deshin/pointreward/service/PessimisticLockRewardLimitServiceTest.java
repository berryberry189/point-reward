package com.homework.deshin.pointreward.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.homework.deshin.pointreward.domain.RewardLimit;
import com.homework.deshin.pointreward.repository.RewardLimitRepository;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PessimisticLockRewardLimitServiceTest {

  @Autowired
  private PessimisticLockRewardLimitService pessimisticLockRewardLimitService;
  @Autowired
  private RewardLimitRepository rewardLimitRepository;

  @BeforeEach
  void before() {
    rewardLimitRepository.save(RewardLimit.builder()
        .payDate(LocalDate.now())
        .limitCount(10)
        .build());
  }

  @AfterEach
  void after() {
    rewardLimitRepository.deleteAll();
  }

  @Test
  void decrease_테스트() {
    pessimisticLockRewardLimitService.decrease(LocalDate.now());

    RewardLimit rewardLimit = rewardLimitRepository.findByPayDate(LocalDate.now()).orElseThrow();

    assertEquals(9, rewardLimit.getLimitCount());
  }

  @Test
  void 동시에_100개의_요청() throws InterruptedException {
    int threadCount = 100;
    // 멀티 스레드 생성
    ExecutorService executorService = Executors.newFixedThreadPool(32);

    // 다른 스레드에서 수행중인 작업이 완료될때 까지 대기할 수 있도록 도와주는 class
    CountDownLatch latch = new CountDownLatch(threadCount);

    for(int i=0; i <threadCount; i++) {
      executorService.submit(() -> {
        try {
          pessimisticLockRewardLimitService.decrease(LocalDate.now());
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    RewardLimit rewardLimit = rewardLimitRepository.findByPayDate(LocalDate.now()).orElseThrow();

    assertEquals(0, rewardLimit.getLimitCount());
  }

}