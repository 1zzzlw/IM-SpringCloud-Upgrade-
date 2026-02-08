package com.zzzlew.server;

import com.zzzlew.pojo.dto.message.FileChunkInfoDTO;
import com.zzzlew.pojo.dto.message.FileMessageDTO;
import com.zzzlew.pojo.dto.message.MessageDTO;
import com.zzzlew.pojo.vo.message.MessageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/15 - 11 - 15 - 23:52
 * @Description: com.zzzlew.zzzimserver.server
 * @version: 1.0
 */
public interface MessageService {

    /**
     * 热数据预加载消息列表，当前限额100条
     *
     * @param conversationIds 会话id列表
     * @param isInit 是否初始化
     * @return 消息列表
     */
    List<MessageVO> initMessageList(String conversationIds, Boolean isInit);

    /**
     * 根据前端最旧消息拉取旧消息到前端数据库中
     *
     * @param conversationId 会话id
     * @return 消息列表
     */
    List<MessageVO> pullMessageList(String conversationId, Long maxMessageId);

    /**
     * 发送消息
     *
     * @param messageDTO 消息dto
     * @return 消息vo
     */
    MessageVO sendMessage(MessageDTO messageDTO);

    /**
     * 上传文件分片
     *
     * @param chunkBlob 文件分片
     * @param fileChunkInfoDTO 文件分片信息
     */
    void uploadFileChunk(MultipartFile chunkBlob, FileChunkInfoDTO fileChunkInfoDTO);

    /**
     * 检查文件分片是否上传完成
     *
     * @param verify 凭证id
     * @param fileId 文件哈希值
     * @return 已上传分片索引列表
     */
    List<Integer> checkUploaded(String verify, String fileId);

    /**
     * 合并文件分片
     *
     * @param fileMessageDTO 文件消息dto
     */
    void mergeFile(FileMessageDTO fileMessageDTO);

    /**
     * 生成上传凭证
     *
     * @param fileId 文件id
     * @return 凭证id
     */
    Map<String, Object> verifyFileUploadToken(String fileId);
}
