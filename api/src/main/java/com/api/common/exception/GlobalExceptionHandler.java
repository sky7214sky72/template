package com.api.common.exception;

import com.api.common.response.ApiResponse;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * . 비즈니스 예외 처리 (직접 정의한 비즈니스 예외 처리)
   */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
    log.warn("BusinessException: {}", e.getMessage());
    ErrorCode errorCode = e.getErrorCode();
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ApiResponse.fail(errorCode.getCode(), e.getMessage()));
  }

  /**
   * . 유효성 검증 실패 처리 (예: @Valid, @Validated 등에서 발생하는 예외 처리)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.warn("MethodArgumentNotValidException: {}", e.getMessage());

    // 어떤 필드에서 에러가 났는지 조합 (예: "email: 이메일 형식이 아닙니다.")
    String errorMessage = e.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity
        .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
        .body(ApiResponse.fail(ErrorCode.INVALID_INPUT_VALUE.getCode(), errorMessage));
  }

  /**
   * . 그 외에 잡히지 않은 모든 예외 처리
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    log.error("UnhandledException: ", e); // 500에러는 로그 필수
    return ResponseEntity
        .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
  }
}
