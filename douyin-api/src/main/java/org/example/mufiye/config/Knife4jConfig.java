package org.example.mufiye.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfoBuilder()
                         .description("mini-douyin接口文档")
                         .termsOfServiceUrl("http://www.mufiye-mini-douyin.com/")
                         .contact(new Contact("mufiye", "https://github.com/mufiye", "mufiye140520@gmail.com"))
                         .version("0.1.0")
                         .build())
            .groupName("version-0.1.0")
            .select()
            .apis(RequestHandlerSelectors.basePackage("org.example.mufiye.controller"))
            .paths(PathSelectors.any())
            .build();
        return docket;
    }
}
