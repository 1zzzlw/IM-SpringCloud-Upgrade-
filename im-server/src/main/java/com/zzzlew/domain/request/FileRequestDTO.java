package com.zzzlew.domain.request;

import com.zzzlew.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: zzzlew
 * @Date: 2025/12/9 - 12 - 09 - 18:20
 * @Description: com.zzzlew.zzzimserver.pojo.dto.message
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileRequestDTO extends Message implements Serializable {
    private String fileName;

    private Long fileSize;

    private Integer fileType;

    @Override
    public int getMessageType() {
        return FileRequestDTO;
    }
}
