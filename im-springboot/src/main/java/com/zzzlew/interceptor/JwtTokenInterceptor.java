package com.zzzlew.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.zzzlew.pojo.dto.user.UserBaseDTO;
import com.zzzlew.properties.Jwtproperties;
import com.zzzlew.utils.JwtUtil;
import com.zzzlew.utils.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zzzlew.constant.RedisConstant.LOGIN_USERINFO_KEY_TTL;
import static com.zzzlew.constant.RedisConstant.LOGIN_USERINFO_REFRESHTOKEN_KEY;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/7 - 11 - 07 - 0:30
 * @Description: com.zzzlew.zzzimserver.interceptor
 * @version: 1.0
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private Jwtproperties jwtproperties;
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        // 检测token是否过期
        String token = request.getHeader(jwtproperties.getTokenName());
        log.info("刷新token是: {}", token);
        // 解析token
        if (jwtUtil.parseJWT(jwtproperties.getFreshSecretKey(), token) == null) {
            log.error("刷新token过期，需要重新登录");
            // 返回状态码需要前端重新登录
            response.setStatus(400);
            return false;
        }

        String refreshTokenKey = LOGIN_USERINFO_REFRESHTOKEN_KEY + token;
        // 获取redis中的用户信息
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(refreshTokenKey);

        // 将用户信息转为用户基本信息DTO UserBaseDTO
        UserBaseDTO userBaseDTO = BeanUtil.copyProperties(userMap, UserBaseDTO.class);
        // 将用户信息存储到ThreadLocal中
        UserHolder.save(userBaseDTO);
        // 刷新token在redis中的过期时间
        stringRedisTemplate.expire(refreshTokenKey, LOGIN_USERINFO_KEY_TTL, TimeUnit.MINUTES);

        // 没有过期，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        // 移除ThreadLocal中的用户信息
        UserHolder.removeUser();
    }
}
