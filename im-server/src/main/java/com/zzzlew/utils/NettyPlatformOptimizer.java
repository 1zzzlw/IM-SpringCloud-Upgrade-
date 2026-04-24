package com.zzzlew.utils;

/**
 * @Auther: zzzlew
 * @Date: 2026/4/15 - 04 - 15 - 22:56
 * @Description: com.zzzlew.utils
 * @version: 1.0
 */

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;


/**
 * Netty 跨平台优化配置器（简版）
 * 自动识别 Linux/Windows 并提供最优配置
 */
@Slf4j
public class NettyPlatformOptimizer {

    public enum OS_TYPE {
        LINUX, WINDOWS, OTHER
    }

    /**
     * 检测当前操作系统类型（仅区分 Linux/Windows）
     */
    public static OS_TYPE detectOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")) return OS_TYPE.LINUX;
        if (osName.contains("windows")) return OS_TYPE.WINDOWS;
        return OS_TYPE.OTHER;
    }

    /**
     * 创建最优的 BossEventLoopGroup
     */
    public static EventLoopGroup createOptimalBossEventLoopGroup() {
        OS_TYPE os = detectOS();
        if (os == OS_TYPE.LINUX && Epoll.isAvailable()) {
            log.info("Linux boss: EpollEventLoopGroup(1)");
            return new EpollEventLoopGroup(1);
        } else {
            log.info("{} boss: NioEventLoopGroup(1)", os);
            return new NioEventLoopGroup(1);
        }
    }

    /**
     * 创建最优的 EventLoopGroup
     */
    public static EventLoopGroup createOptimalEventLoopGroup() {
        OS_TYPE os = detectOS();
        int optimalThreads = getOptimalThreadCount(os);

        if (os == OS_TYPE.LINUX && Epoll.isAvailable()) {
            log.info("Linux + epoll, 线程数: {}", optimalThreads);
            return new EpollEventLoopGroup(optimalThreads);
        } else {
            log.info("{} + NIO, 线程数: {}", os, optimalThreads);
            return new NioEventLoopGroup(optimalThreads);
        }
    }

    /**
     * 获取最优的 ServerSocketChannel 类
     */
    public static Class<? extends ServerSocketChannel> getOptimalServerChannelClass() {
        OS_TYPE os = detectOS();
        if (os == OS_TYPE.LINUX && Epoll.isAvailable()) {
            return EpollServerSocketChannel.class;
        }
        return NioServerSocketChannel.class;
    }

    /**
     * 获取平台最优的线程数
     * Linux: CPU * 2（epoll 多路复用）
     * Windows: CPU 核心数（避免 IOCP 过度切换）
     */
    public static int getOptimalThreadCount(OS_TYPE os) {
        int processors = NettyRuntime.availableProcessors();
        return os == OS_TYPE.WINDOWS ? Math.max(1, processors) : Math.max(1, processors * 2);
    }

    /**
     * 获取平台最优的 SO_BACKLOG 值
     * Linux: 4096（epoll 支持大并发）
     * Windows: 1024（IOCP 限制较多）
     */
    public static int getOptimalBacklog() {
        OS_TYPE os = detectOS();
        return os == OS_TYPE.WINDOWS ? 1024 : 4096;
    }

    /**
     * 获取平台简要日志信息
     */
    public static String getPlatformOptimizationTips() {
        OS_TYPE os = detectOS();
        if (os == OS_TYPE.LINUX && Epoll.isAvailable()) {
            return "系统: Linux, 使用 epoll";
        } else if (os == OS_TYPE.WINDOWS) {
            return "系统: Windows, 使用 NIO";
        } else if (os == OS_TYPE.LINUX) {
            return "系统: Linux, epoll 不可用（使用 NIO）";
        } else {
            return "系统: " + os + ", 使用 NIO";
        }
    }
}