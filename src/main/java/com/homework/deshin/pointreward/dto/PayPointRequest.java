package com.homework.deshin.pointreward.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(description = "포인트 지급 요청 Request DTO")
public class PayPointRequest {

  @NotEmpty
  @ApiModelProperty(value = "멤버 id", required = true, example = "member_1")
  private String memberId;

  public PayPointRequest(@NotEmpty String memberId) {
    this.memberId = memberId;
  }
}
