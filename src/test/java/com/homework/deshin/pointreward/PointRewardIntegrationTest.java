package com.homework.deshin.pointreward;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.deshin.pointreward.dto.PayPointRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointRewardIntegrationTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  final String URI = "/point-reward";

  @Test
  @DisplayName("선착순 포인트 발급")
  void payPointOK() throws Exception {
    PayPointRequest request = new PayPointRequest("member_1");

    mvc.perform(post(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("선착순 포인트 발급 요청 시 멤버 id가 비어있으면 400 상태코드를 반환한다.")
  void payPointRequestFail() throws Exception {
    PayPointRequest request = new PayPointRequest("");

    mvc.perform(post(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @DisplayName("선착순 포인트 발급 목록 내역 조회 시 날짜가 비어있으면 400 상태코드를 반환한다.")
  void getPointRewardListFail() throws Exception {

    mvc.perform(get(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("선착순 포인트 발급 상세 내역 조회 시 포인트 발급 ID가 유효하지 않을 경우 404 상태 코드를 반환한다.")
  void getPointRewardNotFound() throws Exception {
    //given
    String invalidId = "9999";

    //when, then
    mvc.perform(get(URI +"/" + invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }



}
