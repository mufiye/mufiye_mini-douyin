package org.example.mufiye.config;

import org.example.mufiye.interceptor.PassportInterceptor;
import org.example.mufiye.interceptor.UserTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/passport/getSMSCode");
        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/userInfo/modifyUserInfo")
                .addPathPatterns("/userInfo/modifyImage");
    }
}
