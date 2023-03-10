package com.homework.deshin.pointreward.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "point_reward")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PointReward {

  @Id
  @Column(name = "point_reward_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", columnDefinition = "varchar(100) comment '멤버 id'", nullable = false)
  private String memberId;

  @Column(name = "rewarded_point", columnDefinition = "int default 0 comment '지급 포인트'")
  private int rewardedPoint;

  @Column(name = "rewarded_at", nullable = false, updatable = false, columnDefinition = "timestamp comment '포인트 지급일시'")
  @CreationTimestamp
  private LocalDateTime rewardedAt;

  @Builder
  public PointReward(String memberId, int rewardedPoint, LocalDateTime rewardedAt) {
    this.memberId = memberId;
    this.rewardedPoint = rewardedPoint;
    this.rewardedAt = rewardedAt;
  }

}
