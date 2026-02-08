package com.zzzlew.config;

import com.zzzlew.interceptor.JwtTokenInterceptor;
import com.zzzlew.utils.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 17:15
 * @Description: com.zzzlew.zzzimserver.config
 * @version: 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RefreshTokenInterceptor refreshTokenInterceptor;
    @Resource
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刷新Redis缓存拦截器，主要拦截短期token
        registry.addInterceptor(refreshTokenInterceptor).addPathPatterns("/**")
            // 排除登录、注册、验证码、文档页面、Swagger UI、OpenAPI JSON、静态资源、Swagger 资源
            .excludePathPatterns("/login", // 登录接口
                "/login/pendingLogin", // 自动登录接口
                "/commons/refreshToken/**", // 刷新token接口
                "/user/register", // 注册接口
                "/login/verifyCode", // 验证码接口
                "/user/phoneCode", // 手机号验证码接口
                "/message/init/list/**", // 初始化消息表的请求
                "/conversation/init/list", // 初始化会话表的请求
                "/friend/init/list", // 初始化好友表的请求
                "/message/verifyUploadToken/**", // 获取文件上传凭证的请求
                "/message/checkUploaded", // 获取已经上传成功的分块文件
                "/message/uploadChunk", // 上传文件分块接口
                "/message/merge", // 合并文件分块接口
                "/swagger-ui/**", // Swagger 原生 UI
                "/v3/api-docs/**", // OpenAPI JSON
                "/webjars/**", // 静态资源
                "/doc.html/**", // 文档页面
                "/doc.html", "/error/**", "/swagger-resources/**" // Swagger 资源
            ).order(0);

        // 刷新accessToken拦截器，主要拦截长期token
        registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/commons/refreshToken/**", // 拦截刷新token的请求
            "/message/verifyUploadToken/**", // 拦截获取文件上传凭证的请求
            "/message/merge", // 拦截合并文件的请求
            "/message/init/list/**", // 拦截初始化消息表的请求
            "/conversation/init/list", // 拦截初始化会话表的请求
            "/friend/init/list" // 拦截初始化好友表的请求
        ).order(1);
    }
}
