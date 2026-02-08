package com.zzzlew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/9 - 12 - 30 - 21:23
 * @Description: com.zzzlew.zzzimserver.config
 * @version: 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许所有来源的跨域请求
        corsConfiguration.addAllowedOriginPattern("*");
        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许所有 HTTP 方法（GET/POST/PUT/DELETE 等）
        corsConfiguration.addAllowedMethod("*");
        // 允许前端获取响应头中的 Authorization 字段
        corsConfiguration.addExposedHeader("authorization");
        corsConfiguration.addExposedHeader("refreshtoken");
        // 允许携带凭证跨域读取自定义响应头的核心
        corsConfiguration.setAllowCredentials(true);
        // 预检请求缓存（避免重复OPTIONS请求）
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    @Bean
    // 注册 CORS 过滤器
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口生效
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
