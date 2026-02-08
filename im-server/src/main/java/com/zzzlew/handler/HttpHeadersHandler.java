package com.zzzlew.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.zzzlew.domain.UserBaseDTO;
import com.zzzlew.utils.ChannelManageUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

import static com.zzzlew.constant.RedisConstant.LOGIN_USERINFO_KEY;


/**
 * @Auther: zzzlew
 * @Date: 2025/11/18 - 11 - 18 - 22:47
 * @Description: com.zzzlew.zzzimserver.websocket
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {

    // TODO 以后需要通过Dubbo通信调用websocket服务来分离Netty通信层和springboot业务层
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (stringRedisTemplate == null) {
            stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            try {
                log.info("检测到 HttpRequest，开始处理");
                // 获得url路径
                String uri = request.uri();
                String token = getToken(uri);
                // TODO token解析失败，之后加上前端的异常状态
                if (StrUtil.isBlank(token) || token.length() < 32) {
                    log.error("请求路径 {} 中未包含 token 参数，或 token 不合法", uri);
                    ctx.channel().close();
                    return;
                }
                log.info("请求路径 {} 中 token 参数值为 {}", uri, token);
                String tokenKey = LOGIN_USERINFO_KEY + token;
                Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
                if (userMap.isEmpty()) {
                    log.error("请求路径 {} 中 token 参数值 {} 不存在", uri, token);
                    ctx.channel().close();
                    return;
                }
                // 将用户信息转为用户基本信息DTO UserBaseDTO
                UserBaseDTO userBaseDTO = BeanUtil.copyProperties(userMap, UserBaseDTO.class);
                // 打印用户基本信息DTO
                log.info("用户基本信息DTO：{}", userBaseDTO);
                ChannelManageUtil.addChannel(userBaseDTO, ctx.channel());
                log.info("HttpRequest 处理完成，准备升级为 WebSocket 协议");
                ctx.fireChannelRead(msg);
            } catch (Exception e) {
                log.error("HttpRequest 处理异常", e);
                request.release();
                ctx.channel().close();
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public String getToken(String url) {
        if (StrUtil.isBlank(url) || !url.contains("?")) {
            return null;
        }
        String[] queryParams = url.split("\\?");
        if (queryParams.length != 2) {
            return null;
        }
        String[] params = queryParams[1].split("=");
        if (params.length != 2) {
            return null;
        }
        return params[1];
    }
}
