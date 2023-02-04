package com.homework.deshin.pointreward.repository;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRewardRepository extends JpaRepository<PointReward, Long> {

  Optional<PointReward> findByMemberIdAndRewardAtGreaterThanEqual(String memberId, LocalDateTime today);

  Optional<PointReward> findByMemberIdAndRewardAtGreaterThanEqualAndRewardAtLessThan(String memberId, LocalDateTime yesterday, LocalDateTime today);

  List<PointReward> findByRewardAtGreaterThanEqualOrderByRewardAtAsc(LocalDateTime today);

}
