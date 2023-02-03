package com.homework.deshin.pointreward.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.homework.deshin.pointreward.domain.PointReward;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Repository Layer Unit Test
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointRewardRepositoryTest {

  @Autowired PointRewardRepository pointRewardRepository;


  @BeforeEach
  void PointReward_당일데이터삭제() {
    List<PointReward> pointRewardList = pointRewardRepository.findByPayAtGreaterThanEqualOrderByPayAtAsc(LocalDate.now().atStartOfDay());
    pointRewardRepository.deleteAll(pointRewardList);
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트")
  void findByMemberIdAndPayAtGreaterThanEqual(){
    String memberId = "member_1";
    pointRewardRepository.save(PointReward.builder()
        .memberId("member_1")
        .point(100)
        .payAt(LocalDateTime.now())
        .build());

    PointReward pointReward = pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqual(memberId,
        LocalDate.now().atStartOfDay()).orElseThrow();

    assertEquals(memberId, pointReward.getMemberId());
  }

  @Test
  @DisplayName("날짜별 포인트 지급 목록 테스트")
  void findByMemberIdAndPayAtGreaterThanEqualAndPayAtLessThan(){

    String memberId = "member_1";
    Optional<PointReward> pointRewardOpt = pointRewardRepository.findByMemberIdAndPayAtGreaterThanEqualAndPayAtLessThan(memberId,
        LocalDate.now().minusDays(1).atStartOfDay(), LocalDate.now().atStartOfDay());

    assertTrue(pointRewardOpt.isEmpty());
  }

}