package com.project.zipkok.config;

import com.project.zipkok.common.argument_resolver.JwtAuthHandlerArgumentResolver;
//import com.project.zipkok.common.interceptor.JwtAuthInterceptor;
import com.project.zipkok.common.interceptor.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthenticationInterceptor;
    private final JwtAuthHandlerArgumentResolver jwtAuthHandlerArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .order(1)
                .addPathPatterns("/")
                .excludePathPatterns("/oauth/kakao/callback", "");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthHandlerArgumentResolver);
    }
}
