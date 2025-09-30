package com.binebli.demospringai;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class RestConfig {
  @Bean
  public RestClientCustomizer logbookInterceptor(LogbookClientHttpRequestInterceptor interceptor) {
    return restClientBuilder -> restClientBuilder.requestInterceptor(interceptor);
  }
}
