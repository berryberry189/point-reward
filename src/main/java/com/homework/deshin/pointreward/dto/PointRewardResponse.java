package com.homework.deshin.pointreward.dto;

import com.homework.deshin.pointreward.domain.PointReward;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ApiModel(description = "포인트 지급 정보")
@NoArgsConstructor
public class PointRewardResponse {

  @ApiModelProperty(value = "포인트 지급 아이디")
  private Long pointRewardId;

  @ApiModelProperty(value = "멤버 id")
  private String memberId;

  @ApiModelProperty(value = "지급 포인트")
  private int point;

  @ApiModelProperty(value = "포인트 지급일시")
  private LocalDateTime rewardAt;

  public PointRewardResponse(PointReward pointReward) {
    this.pointRewardId = pointReward.getId();
    this.memberId = pointReward.getMemberId();
    this.point = pointReward.getPoint();
    this.rewardAt = pointReward.getRewardAt();
  }

}