package com.homework.deshin.pointreward.controller;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.dto.PointRewardListResponse;
import com.homework.deshin.pointreward.dto.PointRewardRequest;
import com.homework.deshin.pointreward.dto.PointRewardResponse;
import com.homework.deshin.pointreward.service.PointRewardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/point-reward")
@RestController
public class PointRewardController {

  private final PointRewardService pointRewardService;

  @ApiOperation(value = "선착순 포인트 발급")
  @PostMapping("")
  public ResponseEntity<PointRewardResponse> payPointReward(@Valid @RequestBody PointRewardRequest request) {
    return new ResponseEntity<>(pointRewardService.payPointReward(request), HttpStatus.CREATED);
  }

  @ApiOperation(value = "선착순 포인트 목록")
  @GetMapping("")
  public ResponseEntity<PointRewardListResponse> pointRewardList(
      @ApiParam(value = "지급일", example = "2023-02-02")
      @RequestParam(value = "reward_date") LocalDate rewardDate,
      @ApiParam(value = "정렬조건", example = "ASC")
      @RequestParam(value = "sort", required = false) PointRewardSort sort
  ) {
    return new ResponseEntity<>(pointRewardService.getPointRewardList(rewardDate, sort), HttpStatus.OK);
  }

  @ApiOperation(value = "선착순 포인트 상세")
  @GetMapping("/{point_reward_id}")
  public ResponseEntity<PointRewardResponse> pointReward(
      @PathVariable("point_reward_id") Long pointRewardId
  ) {
    return new ResponseEntity<>(pointRewardService.getPointReward(pointRewardId), HttpStatus.OK);
  }


}
