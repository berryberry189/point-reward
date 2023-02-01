package com.homework.deshin.pointreward.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "point_reward")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PointReward {

  @Id
  @Column(name = "point_reward_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id", columnDefinition = "varchar(200) comment '멤버 id'", nullable = false)
  private String memberId;

  @Column(name = "point", columnDefinition = "int default 0 comment '지급 포인트'")
  private int point;

  @Column(name = "pay_at", nullable = false, updatable = false, columnDefinition = "timestamp comment '포인트 지급일시'")
  @CreationTimestamp
  private LocalDateTime payAt;

  @Builder
  public PointReward(String memberId, int point, LocalDateTime payAt) {
    this.memberId = memberId;
    this.point = point;
    this.payAt = payAt;
  }
}
