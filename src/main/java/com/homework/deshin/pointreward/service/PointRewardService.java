package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import com.homework.deshin.pointreward.dto.PointRewardResponse;
import java.time.LocalDate;
import java.util.List;

public interface PointRewardService {

  PointRewardResponse payPointReward(PayPointRequest request);

  List<PointRewardResponse> getPointRewardList(LocalDate rewardDate, PointRewardSort sort);

  PointRewardResponse getPointReward(Long pointRewardId);

}
