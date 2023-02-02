package com.homework.deshin.pointreward.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("POINT REWARD REST API")
        .description("선착순 포인트 지급 REST API Document")
        .build();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors
            .basePackage("com.homework.deshin.pointreward.controller"))
        .paths(PathSelectors.any())
        .build();
  }
}
