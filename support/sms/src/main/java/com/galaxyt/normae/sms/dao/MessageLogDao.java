package com.galaxyt.normae.sms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxyt.normae.sms.pojo.po.Message;
import org.springframework.stereotype.Repository;

/**
 * 短信发送记录 dao
 * @author zhouqi
 * @date 2020/8/24 17:26
 * @version v1.0.0
 * @Description
 *
 */
@Repository
public interface MessageLogDao extends BaseMapper<Message> {
}
