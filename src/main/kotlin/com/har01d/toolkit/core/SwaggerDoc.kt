package com.har01d.toolkit.core

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerDoc {
    @Bean
    fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)//
                .produces(setOf("application/json", "text/plain"))//
                .consumes(setOf("application/json"))//
                .apiInfo(apiInfo())//
                .pathMapping("/")//
                .select()//
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))//
                .paths(PathSelectors.any())//
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()//
                .title("Utility API")//
                .description("Utility RESTful APIs")//
                .version("1.0")//
                .build()
    }
}
