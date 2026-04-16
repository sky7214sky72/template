package com.api.presentation.controller;

import com.api.global.response.ApiResponse;
import com.core.common.exception.BusinessException;
import com.core.common.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  // 1. 성공 테스트
  @GetMapping("/success")
  public ApiResponse<String> successTest() {
    return ApiResponse.success("완벽하게 작동합니다!");
  }

  // 2. 비즈니스 에러 테스트
  @GetMapping("/error")
  public ApiResponse<Void> errorTest() {
    throw new BusinessException(ErrorCode.USER_NOT_FOUND);
  }
}
