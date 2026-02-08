package com.zzzlew.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Auther: zzzlew
 * @Date: 2025/10/13 - 10 - 13 - 23:49
 * @Description: itcast.protocol
 * @version: 1.0
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        this(128 * 1024, 12, 4, 0, 0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
        int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
