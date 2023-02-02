package com.homework.deshin.pointreward.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.domain.PointReward;
import com.homework.deshin.pointreward.domain.PointRewardDto;
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
import javax.persistence.EntityNotFoundException;
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
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqualOrderByPayAtAsc(today.atStartOfDay());
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  void 같은_ID로_중복_요청하는_경우_에러발생() {
    PayPointRequest request = new PayPointRequest("member_1");
    pointRewardService.payPointReward(request);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> pointRewardService.payPointReward(request));

    assertEquals("이미 참여완료 되었습니다.", exception.getMessage());
  }

  @Test
  void 선착순_초과로_요청하는_경우_에러발생() {
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
    int threadCount = 1000;
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
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqualOrderByPayAtAsc(LocalDate.now().atStartOfDay());

    assertEquals(0, rewardLimit.getLimitCount());
    assertEquals(10, pointRewardList.size());

  }

  @Test
  void 전날에_출석한_이력있으면_전날포인트_더하기_100포인트_제공() {
    String memberId = "member_1";
    PayPointRequest request = new PayPointRequest(memberId);
    pointRewardService.payPointReward(request);

    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqualOrderByPayAtAsc(today.atStartOfDay());

    assertEquals(200, pointRewardList.get(0).getPoint());

  }

  @Test
  void 날짜지정_목록조회_정렬확인() {
    PayPointRequest request1 = new PayPointRequest("member_1");
    PayPointRequest request2 = new PayPointRequest("member_2");
    pointRewardService.payPointReward(request1);
    pointRewardService.payPointReward(request2);

    List<PointRewardDto> pointRewardAscList = pointRewardService.getPointRewardList(today, PointRewardSort.ASC);
    List<PointRewardDto> pointRewardDescList = pointRewardService.getPointRewardList(today, PointRewardSort.DESC);

    assertEquals(2, pointRewardAscList.size());
    assertEquals("member_1", pointRewardAscList.get(0).getMemberId());
    assertEquals("member_2", pointRewardDescList.get(0).getMemberId());
  }

  @Test
  void 상세_조회시_ID가_유효하지_않은_경우_EntityNotFoundException발생() {
    PayPointRequest request = new PayPointRequest("member_1");
    PointRewardDto savedPointRewardDto = pointRewardService.payPointReward(request);

    PointRewardDto pointReward = pointRewardService.getPointReward(savedPointRewardDto.getPointRewardId());

    assertEquals(savedPointRewardDto.getPointRewardId(), pointReward.getPointRewardId());
    assertThrows(EntityNotFoundException.class, () -> pointRewardService.getPointReward(1000L));

  }



}