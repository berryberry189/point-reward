package com.homework.deshin.pointreward.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reward_limit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardLimit {

  @Id
  @Column(name = "reward_limit_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "limit_count", columnDefinition = "int default 0 comment '포인트 지급 제한 회원수'")
  private int limitCount;

  @Column(name = "pay_date", columnDefinition = "timestamp comment '포인트 지급 일'", nullable = false)
  private LocalDate payDate;


  public void decrease() {
    if (this.limitCount < 1) {
      throw new IllegalArgumentException("선착순이 종료되었습니다.");
    }
    this.limitCount--;
  }

  @Builder
  public RewardLimit(int limitCount, LocalDate payDate) {
    this.limitCount = limitCount;
    this.payDate = payDate;
  }
}
