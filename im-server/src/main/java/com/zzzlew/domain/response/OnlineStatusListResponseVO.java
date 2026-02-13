package com.zzzlew.domain.response;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2026/2/12 - 02 - 12 - 23:30
 * @Description: com.zzzlew.domain.response
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OnlineStatusListResponseVO extends Message {

    /**
     * 在线好友id集合
     *
     */
    private List<String> friendIdList;

    @Override
    public int getMessageType() {
        return OnlineStatusListResponseVO;
    }
}
