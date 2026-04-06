package com.zzzlew.aop;

import com.zzzlew.annotaion.UrlLimit;
import com.zzzlew.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: zzzlew
 * @Date: 2026/4/2 - 04 - 02 - 23:17
 * @Description: com.zzzlew.aop
 * @version: 1.0
 */
@Aspect
@Component
@Slf4j
public class UrlLimitAspect {

    private final ConcurrentHashMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.zzzlew.annotaion.UrlLimit)")
    public void rateLimitPointcut() {
    }

    @Around("rateLimitPointcut() && @annotation(urlLimit)")
    public Object around(ProceedingJoinPoint joinPoint, UrlLimit urlLimit) throws Throwable {
        String key = getKey(joinPoint);
        AtomicInteger count = counterMap.computeIfAbsent(key, k -> new AtomicInteger(0));

        if (count.incrementAndGet() > urlLimit.maxRequests()) {
            throw new RuntimeException("请求过于频繁");
        }

        return joinPoint.proceed();
    }

    private String getKey(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String key;
        if (UserHolder.getUser() != null) {
            key = "user:" + UserHolder.getUser().getId();
        } else {
            key = "ip:" + request.getRemoteAddr();
        }
        return key + ":" + request.getRequestURI();
    }

    @Scheduled(fixedRate = 60000)
    public void clearCounter() {
        counterMap.clear();
    }
}