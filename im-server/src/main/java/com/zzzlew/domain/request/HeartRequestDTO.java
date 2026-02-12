package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2026/2/11 - 02 - 11 - 0:38
 * @Description: com.zzzlew.domain.request
 * @version: 1.0
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class HeartRequestDTO extends Message implements Serializable {
    @Override
    public int getMessageType() {
        return HeartRequestDTO;
    }
}
