package com.galaxyt.normae.sms.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.galaxyt.normae.sms.enums.MessageType;
import com.galaxyt.normae.sms.enums.SmsPlatform;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短信发送记录
 * @author zhouqi
 * @date 2020/5/28 17:52
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/28 17:52     zhouqi          v1.0.0           Created
 *
 */
@Data
@TableName("t_message_log")
public class MessageLog {

    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;

    /**
     * 手机号码
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 短信发送的内容
     */
    @TableField("message")
    private String message;

    /**
     * 短信发送平台
     */
    @TableField("sms_platform")
    private SmsPlatform smsPlatform;

    /**
     * 短信发送类型
     */
    @TableField("message_type")
    private MessageType messageType;

    /**
     * 发送时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

}
