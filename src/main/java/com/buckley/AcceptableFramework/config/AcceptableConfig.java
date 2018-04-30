package com.buckley.AcceptableFramework.config;

import com.buckley.AcceptableFramework.interceptors.RecorderInterceptor;
import com.buckley.AcceptableFramework.utils.RequestResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AcceptableConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new RecorderInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public RequestResponseWriter requestResponseWriter() {
        return new RequestResponseWriter();
    }

}
