package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.constant.PointRewardSort;
import com.homework.deshin.pointreward.domain.PointRewardDto;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import java.time.LocalDate;
import java.util.List;

public interface PointRewardService {

  PointRewardDto payPointReward(PayPointRequest request);

  List<PointRewardDto> getPointRewardList(LocalDate rewardDate, PointRewardSort sort);

  PointRewardDto getPointReward(Long pointRewardId);

}
