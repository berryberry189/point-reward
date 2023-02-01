package com.homework.deshin.pointreward.service;

import com.homework.deshin.pointreward.dto.PayPointRequest;

public interface PointRewardService {

  void addWaitQueue(PayPointRequest request);

}
