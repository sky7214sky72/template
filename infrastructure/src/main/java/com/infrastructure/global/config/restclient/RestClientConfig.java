package com.infrastructure.global.config.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient customRestClient() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(3000); // 3초 (네이버 서버와 연결을 맺는 데까지 기다려주는 시간)
    factory.setReadTimeout(5000);    // 5초 (연결은 됐고, 실제 데이터를 응답받을 때까지 기다려주는 시간)

    return RestClient.builder()
        .requestFactory(factory)
        .defaultHeader("Accept", "application/json")
        .build();
  }
}
