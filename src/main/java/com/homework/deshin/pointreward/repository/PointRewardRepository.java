package com.homework.deshin.pointreward.repository;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRewardRepository extends JpaRepository<PointReward, Long> {

  Optional<PointReward> findByMemberIdAndRewardAtBetween(String memberId, LocalDateTime start, LocalDateTime end);

  List<PointReward> findAllByRewardAtBetweenOrderByRewardAtAsc(LocalDateTime start, LocalDateTime end);

  List<PointReward> findAllByRewardAtBetweenOrderByRewardAtDesc(LocalDateTime start, LocalDateTime end);

}
