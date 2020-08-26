package com.galaxyt.normae.sms.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.galaxyt.normae.sms.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短信发送记录
 * @author zhouqi
 * @date 2020/8/24 17:22
 * @version v1.0.0
 * @Description
 *
 */
@Data
@TableName("t_message")
public class Message {

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
    @TableField("content")
    private String content;

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
