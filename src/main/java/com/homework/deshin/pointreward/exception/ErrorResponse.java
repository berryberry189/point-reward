package com.homework.deshin.pointreward.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

  //상태 코드
  private final int status;
  //에러 유형
  private final String error;
  //에러 메시지
  private final String message;

  public ErrorResponse(int status, String error, String message) {
    this.status = status;
    this.error = error;
    this.message = message;
  }
}