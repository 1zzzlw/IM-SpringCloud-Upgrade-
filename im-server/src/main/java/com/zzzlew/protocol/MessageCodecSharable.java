package com.zzzlew.protocol;

import cn.hutool.extra.spring.SpringUtil;
import com.zzzlew.domain.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import com.zzzlew.properties.SerializerProperties;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/19 - 11 - 19 - 15:45
 * @Description: com.zzzlew.zzzimserver.protocol
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    SerializerProperties properties = SpringUtil.getBean(SerializerProperties.class);
    Serializer.Algorithm algorithm = properties.getAlgorithm();

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> list) throws Exception {

        ByteBuf out = ctx.alloc().buffer();
        // 1. 4个字节的魔数
        out.writeBytes(new byte[] {1, 2, 3, 4});
        // 2. 1个字节的版本号
        out.writeByte(1);
        // 3. 1个字节的序列化方式记号 ordinal()获取枚举的顺序，默认从0开始，JSON为0，Java为1
        out.writeByte(algorithm.ordinal());
        // 4 1个字节的消息类型
        out.writeByte(message.getMessageType());
        // 5 4个字节的消息序列号
        out.writeInt(message.getSequenceId());
        // 无意义，对齐填充，保证和消息长度一致
        out.writeByte(0xff);
        // 6. 获取内容中的字节数组，调用自定义序列化类中的 serialize 方法，已经在枚举中实现
        byte[] bytes = algorithm.serialize(message);
        // 7. 将内容长度写入 4个字节
        out.writeInt(bytes.length);
        // 8. 将内容写入 ByteBuf
        out.writeBytes(bytes);
        // 9. 将 ByteBuf 写入 list
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("MessageCodecSharable开始解码，可读字节: {}", byteBuf.readableBytes());

        // 1. 先读取魔数 4 个字节
        int magicNum = byteBuf.readInt();
        // 2. 读取 1 个字节的版本号
        byte version = byteBuf.readByte();
        // 3. 读取 1 个字节的序列化方式号，根据 ordinal() 其中 JSON为0，Java为1
        byte serializerOrdinal = byteBuf.readByte();
        // 4. 读取 1 个字节的消息类型，根据这个来反序列化为对应的消息类型
        byte messageType = byteBuf.readByte();
        // 5. 读取 4 个字节的消息序列号
        int sequenceId = byteBuf.readInt();
        // 取出无意义的 4 个字节
        byteBuf.readByte();
        // 剩下的就是消息正文，先读取长度
        int length = byteBuf.readInt();
        // 6. 获取内容中的字节数组
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);

        // 首先先拿到所有的枚举类型，然后根据 serializerOrdinal 枚举序号来选择对应的序列化方式
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerOrdinal];
        // 确定具体的消息类型
        Class<?> messageClass = Message.getMessageClass(messageType);
        // 根据消息类型来反序列化字节数组为对应的消息类型
        Object message = algorithm.deserialize(messageClass, bytes);
        // 7. 将反序列化后的消息添加到 list 中
        log.info("MessageCodecSharable解码完成，消息类型: {}, 序列号: {}", messageType, sequenceId);
        list.add(message);
    }
}
