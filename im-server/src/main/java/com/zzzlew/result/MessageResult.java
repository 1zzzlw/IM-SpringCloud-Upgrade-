package com.zzzlew.result;

import com.zzzlew.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2026/2/8 - 02 - 08 - 21:57
 * @Description: com.zzzlew.result
 * @version: 1.0
 */
@Data
@AllArgsConstructor
public class MessageResult {
    private Message response;
    private List<Long> receiverIds;

    public static MessageResult single(Message response, Long receiverId) {
        return new MessageResult(response, Collections.singletonList(receiverId));
    }

    public static MessageResult multiple(Message response, List<Long> receiverIds) {
        return new MessageResult(response, receiverIds);
    }
}
