package com.galaxyt.normae.sms.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.galaxyt.normae.sms.enums.MessageType;
import com.galaxyt.normae.sms.enums.SmsPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 阿里云发送短信服务
 * @author zhouqi
 * @date 2020/5/28 16:54
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/28 16:54     zhouqi          v1.0.0           Created
 *
 */
@Slf4j
@Service
public class AliyunService {

    @Autowired
    private MessageLogService messageLogService;


    //=================== 阿里云短信发送 SDK 参数 ==============================

    @Value("${aliyuncs.profile.region-id}")
    private String regionId;

    @Value("${aliyuncs.profile.access-key-id}")
    private String accessKeyId;

    @Value("${aliyuncs.profile.secret}")
    private String secret;

    @Value("${aliyuncs.profile.endpoint-name}")
    private String endpointName;

    @Value("${aliyuncs.profile.product}")
    private String product;

    @Value("${aliyuncs.profile.domain}")
    private String domain;

    @Value("${aliyuncs.profile.version}")
    private String version;

    @Value("${aliyuncs.profile.action}")
    private String action;

    @Value("${aliyuncs.profile.sign-name}")
    private String signName;

    @Value("${sun.net.client.defaultConnectTimeout}")
    private String connectTimeout;

    @Value("${sun.net.client.defaultReadTimeout}")
    private String readTimeout;

    /**
     * 短信发送
     * @param phoneNumber
     * @param messageType
     * @param messageParam
     * @return
     */
    @Transactional
    public boolean sendMessage(String phoneNumber, MessageType messageType, String messageParam) {

        try {
            DefaultProfile profile = DefaultProfile.getProfile(this.regionId, this.accessKeyId, this.secret);
            DefaultProfile.addEndpoint(this.endpointName, this.regionId, this.product, this.domain);

            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain(this.domain);
            request.setVersion(this.version);
            request.setAction(this.action);
            //设置超时时间-可自行调整
            System.setProperty("sun.net.client.defaultConnectTimeout", this.connectTimeout);
            System.setProperty("sun.net.client.defaultReadTimeout", this.readTimeout);
            request.putQueryParameter("PhoneNumbers", phoneNumber);
            request.putQueryParameter("TemplateCode", messageType.getTemplateCode());
            request.putQueryParameter("SignName", this.signName);
            request.putQueryParameter("TemplateParam", messageParam);
            CommonResponse response = client.getCommonResponse(request);
            if (response != null && response.getHttpStatus() == 200) {//验证码发送成功
                //记录短信发送记录
                this.messageLogService.addLog(phoneNumber, messageType, messageParam, SmsPlatform.ALIYUN);
                return Boolean.TRUE;
            }

        } catch (ServerException e) {
            //ignore
            e.printStackTrace();
        } catch (ClientException e) {
            //ignore
            e.printStackTrace();
        }
        log.info("发送失败");
        return Boolean.FALSE;
    }



}
