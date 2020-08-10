package com.galaxyt.normae.sms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.normae.sms.pojo.po.MessageLog;
import org.springframework.stereotype.Repository;

/**
 * 短信发送记录 dao
 * @author zhouqi
 * @date 2020/5/28 18:06
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/28 18:06     zhouqi          v1.0.0           Created
 *
 */
@Repository
public interface MessageLogDao extends BaseMapper<MessageLog> {
}
