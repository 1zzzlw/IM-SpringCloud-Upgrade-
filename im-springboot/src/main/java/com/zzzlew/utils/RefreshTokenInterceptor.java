package com.zzzlew.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.zzzlew.pojo.dto.user.UserBaseDTO;
import com.zzzlew.properties.Jwtproperties;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zzzlew.constant.RedisConstant.*;


/**
 * @Auther: zzzlew
 * @Date: 2025/11/12 - 11 - 12 - 16:38
 * @Description: com.zzzlew.zzzimserver.utils
 * @version: 1.0
 */
@Slf4j
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private Jwtproperties jwtproperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        System.out.println("Interceptor triggered: " + request.getRequestURI());
        // 获取请求头中的短期token
        String token = request.getHeader(jwtproperties.getTokenName());
        log.info("请求头中的短期token为：{}", token);

        // 检查token是否为空
        if (StrUtil.isBlank(token)) {
            // 只做刷新token在redis中的过期时间操作，不做拦截
            return true;
        }

        // 解析token判断是否过期
        if (jwtUtil.parseJWT(jwtproperties.getAccessSecretKey(), token) == null) {
            // 说明令牌过期
            response.setStatus(401);
            return false;
        }

        // redis中键的格式为：login:user:Info:token
        String tokenKey = LOGIN_USERINFO_KEY + token;
        // 从redis信息中获取用户信息
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
        // 检查用户信息是否为空,如果为空,说明token过期,只做刷新token在redis中的过期时间操作,不做拦截
        if (userMap.isEmpty()) {
            // 只做刷新token在redis中的过期时间操作，不做拦截
            return true;
        }

        // 将用户信息转为用户基本信息DTO UserBaseDTO
        UserBaseDTO userBaseDTO = BeanUtil.copyProperties(userMap, UserBaseDTO.class);
        // 将用户信息存储到ThreadLocal中
        UserHolder.save(userBaseDTO);
        // 刷新token在redis中的过期时间
        stringRedisTemplate.expire(tokenKey, LOGIN_USERINFO_KEY_TTL, TimeUnit.MINUTES);

        // 从UserBaseDTO中获取userId
        Long userId = userBaseDTO.getId();
        // 存放token的Set的key
        String userTokenSetKey = LOGIN_USER_TOKEN_LIST_KEY + userId;
        stringRedisTemplate.expire(userTokenSetKey, LOGIN_USER_TOKEN_LIST_KEY_TTL, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        // 移除ThreadLocal中的用户信息
        UserHolder.removeUser();
    }

}
