package com.zzzlew.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.zzzlew.protocol.Serializer;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 19:23
 * @Description: com.zzzlew.zzzimserver.properties
 * @version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "serializer")
public class SerializerProperties {
    // 序列化算法，配置文件中没有配置就默认Java序列化
    private Serializer.Algorithm algorithm = Serializer.Algorithm.Java;
}
