package com.homework.deshin.pointreward.repository;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRewardRepository extends JpaRepository<PointReward, Long> {

  Optional<PointReward> findByMemberIdAndRewardedAtBetween(String memberId, LocalDateTime start, LocalDateTime end);

  List<PointReward> findAllByRewardedAtBetweenOrderByRewardedAtAsc(LocalDateTime start, LocalDateTime end);

  List<PointReward> findAllByRewardedAtBetweenOrderByRewardedAtDesc(LocalDateTime start, LocalDateTime end);

}
