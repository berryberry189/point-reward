package com.homework.deshin.pointreward.exception;

import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 유효하지 않은 입력 값 오류 (400)
   */
  @ExceptionHandler(value = {IllegalArgumentException.class, TypeMismatchException.class})
  public ResponseEntity<ErrorResponse> invalidArgumentException(RuntimeException e) {
    log.error("invalidArgumentException", e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }

  /**
   * Validation을 통한 Request 값 검증 오류 (400)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("methodArgumentNotValidException", e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    //유효성 검증 실패한 필드, 에러 메시지 정보
    String errorField = e.getBindingResult().getFieldError().getField();
    String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
    String errorMessage = errorField + ": " + defaultMessage;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), errorMessage), status);
  }

  /**
   * 엔티티 조회 실패 오류 (404)
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {
    log.error("entityNotFoundException", e);
    HttpStatus status = HttpStatus.NOT_FOUND;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }

  /**
   * 서버 장애에 대한 공통 처리 (500)
   */
  @ExceptionHandler(value = {Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> unexpectedException(RuntimeException e) {
    log.error("unexpectedException", e);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    return new ResponseEntity<>(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()), status);
  }

}
