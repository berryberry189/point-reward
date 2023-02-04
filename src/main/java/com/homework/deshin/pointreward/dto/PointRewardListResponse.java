package com.homework.deshin.pointreward.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ApiModel(description = "포인트 지급 목록")
@NoArgsConstructor
public class PointRewardListResponse {

  @ApiModelProperty(value = "포인트 지급 목록")
  private List<PointRewardResponse> pointRewardList;

  public PointRewardListResponse(List<PointRewardResponse> pointRewardList) {
    this.pointRewardList = pointRewardList;
  }

}
