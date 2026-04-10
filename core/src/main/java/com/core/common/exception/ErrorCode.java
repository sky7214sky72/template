package com.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  //1. 공통 에러
  INVALID_INPUT_VALUE(400, "C001", "잘못된 입력 값입니다."),
  INTERNAL_SERVER_ERROR(500, "C002", "서버 내부 오류가 발생했습니다."),

  //2. 인증/인가 에러
  UNAUTHORIZED(401, "A001", "인증되지 않은 사용자입니다."),
  FORBIDDEN(403, "A002", "권한이 없는 사용자입니다."),

  //3. 유저 도메인 에러
  USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
  DUPLICATE_EMAIL(409, "U002", "이미 가입된 이메일입니다.");

  private final int status;
  private final String code;
  private final String message;
}
