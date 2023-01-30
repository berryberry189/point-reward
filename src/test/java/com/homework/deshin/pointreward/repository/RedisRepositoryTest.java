package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisRepositoryTest {

  @Autowired RedisRepository redisRepository;

  private final String key = LocalDate.now().toString();

  @AfterEach
  void deleteByKey() {
    redisRepository.deleteByKey(key);
  }

  @Test
  void value가_같으면_Distinct된다() {
    // given
    String value = "memberId";

    // when
    redisRepository.add(key, value, System.currentTimeMillis());
    redisRepository.add(key, value, System.currentTimeMillis());

    // then
    assertEquals(1, redisRepository.getSize(key));

  }

  @Test
  void value가_다르면_각각_저장된다() {
    // given
    String value1 = "memberId1";
    String value2 = "memberId2";

    // when
    redisRepository.add(key, value1, System.currentTimeMillis());
    redisRepository.add(key, value2, System.currentTimeMillis());

    // then
    assertEquals(2, redisRepository.getSize(key));

  }

  @Test
  void score에_따라서_순서가_결정된다() {
    // given
    String value1 = "memberId1";
    long score1 = 10;
    String value2 = "memberId2";
    long score2 = 20;
    String value3 = "memberId3";
    long score3 = 30;

    // when
    redisRepository.add(key, value1, score1);
    redisRepository.add(key, value2, score2);
    redisRepository.add(key, value3, score3);

    // then
    assertEquals(0, redisRepository.getRank(key, value1));
    assertEquals(1, redisRepository.getRank(key, value2));
    assertEquals(2, redisRepository.getRank(key, value3));

  }



  @Test
  void range_startRank이상_endRank이하의_값이_출력된다() {
    // given
    String value1 = "memberId1";
    String value2 = "memberId2";
    String value3 = "memberId3";

    // when
    redisRepository.add(key, value1, System.currentTimeMillis());
    redisRepository.add(key, value2, System.currentTimeMillis());
    redisRepository.add(key, value3, System.currentTimeMillis());

    // then
    Set<Object> set = redisRepository.range(key, 0, 1);
    assertTrue(set.contains(value1));
    assertTrue(set.contains(value2));

  }


}