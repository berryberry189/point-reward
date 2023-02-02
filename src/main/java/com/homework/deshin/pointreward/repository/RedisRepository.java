package com.homework.deshin.pointreward.repository;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

  private final RedisTemplate<String,Object> redisTemplate;

  public long getSize(String key) {
    return redisTemplate.opsForZSet().size(key);
  }

  public void add (String key, String value, long now) {
    redisTemplate.opsForZSet().add(key, value, (int) now);
  }

  public void addIfAbsent (String key, String value, long now) {
    redisTemplate.opsForZSet().addIfAbsent(key, value, (int) now);
  }

  public Set<Object> range(String key, long start, long end) {
    return redisTemplate.opsForZSet().range(key, start, end);
  }

  public Long getRank(String key, Object o) {
    return redisTemplate.opsForZSet().rank(key, o);
  }

  public void deleteByKey(String key) {
    redisTemplate.delete(key);
  }

  public double getScore(String key, Object o) {
    return redisTemplate.opsForZSet().score(key, o);
  }

  public void remove(String key, Object o) {
    redisTemplate.opsForZSet().remove(key, o);
  }

}
