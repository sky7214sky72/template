package com.api.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

  private final boolean success;
  private final String code;
  private final String message;
  private final T data;

  // 성공 시 응답 (데이터 있음)
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, "S000", "요청이 성공적으로 처리되었습니다.", data);
  }

  // 성공시 응답 (데이터 없음)
  public static ApiResponse<Void> success() {
    return new ApiResponse<>(true, "S000", "요청이 성공적으로 처리되었습니다.", null);
  }

  // 실패 시 응답 (에러 코드 기반)
  public static <T> ApiResponse<T> fail(String code, String message) {
    return new ApiResponse<>(false, code, message, null);
  }
}
