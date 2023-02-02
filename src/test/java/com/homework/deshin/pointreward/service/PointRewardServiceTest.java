package com.homework.deshin.pointreward.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.domain.RewardLimit;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.repository.PointRewardRepository;
import com.homework.deshin.pointreward.repository.RewardLimitRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointRewardServiceTest {

  @Autowired
  PointRewardService pointRewardService;

  @Autowired
  PointRewardRepository pointRewardRepository;

  @Autowired
  RewardLimitRepository rewardLimitRepository;

  private LocalDate today = LocalDate.now();

  @BeforeEach
  void initRewardLimit() {
    Optional<RewardLimit> rewardLimitOpt = rewardLimitRepository.findByPayDate(today);
    if (rewardLimitOpt.isPresent()) {
      rewardLimitRepository.delete(rewardLimitOpt.get());
    }
    rewardLimitRepository.save(rewardLimitRepository.save(RewardLimit.builder()
        .payDate(LocalDate.now())
        .limitCount(10)
        .build()));
  }

  @BeforeEach
  void PointReward_당일데이터삭제() {
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqual(today.atStartOfDay());
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  void 같은_ID로_중복_요청하는_경우() {
    PayPointRequest request = new PayPointRequest("member_1");
    pointRewardService.payPointReward(request);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> pointRewardService.payPointReward(request));

    assertEquals("이미 참여완료 되었습니다.", exception.getMessage());
  }

  @Test
  void 선착순_초과로_요청하는_경우() {
    for (int i = 1; i <= 10; i++) {
      PayPointRequest request = new PayPointRequest("member_" + i);
      pointRewardService.payPointReward(request);
    }

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> pointRewardService.payPointReward(new PayPointRequest("member_11")));

    assertEquals("선착순이 종료되었습니다.", exception.getMessage());
  }

  @Test
  void 한번에_많은_요청이_있는_경우() throws InterruptedException {
    int threadCount = 50;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 1; i <= threadCount; i++) {
      int num = i;
      executorService.submit(() -> {
        try {
          PayPointRequest request = new PayPointRequest("member_" + num);
          pointRewardService.payPointReward(request);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    RewardLimit rewardLimit = rewardLimitRepository.findByPayDate(LocalDate.now()).orElseThrow();
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqual(LocalDate.now().atStartOfDay());

    assertEquals(0, rewardLimit.getLimitCount());
    assertEquals(10, pointRewardList.size());

  }

  @Test
  void 전날에_출석한_이력있으면_전날포인트_더하기_100포인트_제공() {
    String memberId = "member_1";
    PayPointRequest request = new PayPointRequest(memberId);
    pointRewardService.payPointReward(request);

    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqual(today.atStartOfDay());

    assertEquals(200, pointRewardList.get(0).getPoint());

  }


}