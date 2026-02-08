package com.zzzlew.protocol;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 16:48
 * @Description: com.zzzlew.zzzimserver.protocol
 * @version: 1.0
 */

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 序列化器，用于扩展不同的序列化方式，如 JSON、Kryo、Hessian 等
 */
public interface Serializer {
    /**
     * 序列化算法
     *
     * @param obj 待序列化的对象
     * @return 序列化后的字节数组
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化算法
     *
     * @param bytes 待反序列化的字节数组
     * @param clazz 目标对象的类类型
     * @param <T> 目标对象的类型
     * @return 反序列化后的对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    enum Algorithm implements Serializer {
        // 枚举类型的默认顺序从0开始

        // 默认顺序为 0
        Json {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                // 将字节数组转换为字符串
                String json = new String(bytes, StandardCharsets.UTF_8);
                // 将JSON字符串反序列化为对象
                return JSON.parseObject(json, clazz);
            }

            @Override
            public <T> byte[] serialize(T object) {
                // 将object对象转化为JSON字符串
                String json = JSON.toJSONString(object);
                // 将JSON字符串转化为字节数组，并使用UTF-8编码，确定编码一致
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                return bytes;
            }
        },

        // 默认顺序为 1
        Java {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T)ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败", e);
                }
            }

            @Override
            public <T> byte[] serialize(T object) {
                try {
                    // 获取内容的字节数组
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }
        }

    }
}
