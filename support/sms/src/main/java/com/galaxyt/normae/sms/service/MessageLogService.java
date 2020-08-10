package com.galaxyt.normae.sms.service;

import com.galaxyt.normae.sms.dao.MessageLogDao;
import com.galaxyt.normae.sms.enums.MessageType;
import com.galaxyt.normae.sms.enums.SmsPlatform;
import com.galaxyt.normae.sms.pojo.po.MessageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 消息记录 service
 * @author zhouqi
 * @date 2020/5/29 9:18
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/29 9:18     zhouqi          v1.0.0           Created
 *
 */
@Service
public class MessageLogService {



    @Autowired
    private MessageLogDao messageLogDao;


    /**
     * 记录短信发送记录
     *
     * @param phoneNumber     电话号码
     * @param messageType 短信类型
     * @param message         发送的内容
     * @param smsPlatform     短信服务商
     */
    @Transactional
    public void addLog(String phoneNumber, MessageType messageType, String message, SmsPlatform smsPlatform) {

        MessageLog messageLog = new MessageLog();

        messageLog.setPhoneNumber(phoneNumber);
        messageLog.setMessage(message);
        messageLog.setSmsPlatform(smsPlatform);
        messageLog.setMessageType(messageType);
        messageLog.setCreateTime(LocalDateTime.now());

        this.messageLogDao.insert(messageLog);
    }


}
