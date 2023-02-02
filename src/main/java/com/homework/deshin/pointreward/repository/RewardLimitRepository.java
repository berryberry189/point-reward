package com.homework.deshin.pointreward.repository;

import com.homework.deshin.pointreward.domain.RewardLimit;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface RewardLimitRepository extends JpaRepository<RewardLimit, Long> {

  Optional<RewardLimit> findByPayDate(LocalDate payDate);


  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  @Query("select r from RewardLimit r where r.payDate = :payDate")
  RewardLimit findByPayDateWithPessimisticLock(@Param("payDate") LocalDate payDate);

}
