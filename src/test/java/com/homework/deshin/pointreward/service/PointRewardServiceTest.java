package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.repository.RedisRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PointRewardServiceTest {

  @Autowired
  PointRewardService pointRewardService;
  @Autowired
  RedisRepository redisRepository;

  private LocalDate today = LocalDate.now();


  @AfterEach
  void deleteByKey() {
    redisRepository.deleteByKey(today.toString());
  }

  @Test
  void 대기열_추가_100명_중복_5명() throws InterruptedException {

    final int entryPeople = 100;

    final CountDownLatch countDownLatch = new CountDownLatch(entryPeople);

    List<Thread> workers = new ArrayList<>();
    workers.add(new Thread(new AddQueueWorker(countDownLatch, "memberId_1")));
    workers.add(new Thread(new AddQueueWorker(countDownLatch, "memberId_2")));
    workers.add(new Thread(new AddQueueWorker(countDownLatch, "memberId_3")));
    workers.add(new Thread(new AddQueueWorker(countDownLatch, "memberId_4")));
    workers.add(new Thread(new AddQueueWorker(countDownLatch, "memberId_5")));
    for(int i=1; i<=entryPeople; i++) {
      workers.add(new Thread(new AddQueueWorker(countDownLatch, "memberId_"+i)));
    }
    workers.forEach(Thread::start);
    countDownLatch.await();

    long totalSize = redisRepository.getSize(today.toString());
    assertEquals(entryPeople, totalSize);

  }


  private class AddQueueWorker implements Runnable {

    private CountDownLatch countDownLatch;
    private String memberId;

    public AddQueueWorker(CountDownLatch countDownLatch, String memberId) {
      this.countDownLatch = countDownLatch;
      this.memberId = memberId;
    }

    @Override
    public void run() {
      pointRewardService.addWaitQueue(new PayPointRequest(memberId));
      countDownLatch.countDown();
    }
  }




}