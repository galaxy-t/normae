package com.galaxyt.normae.file.web;

import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import com.galaxyt.normae.file.pojo.vo.FileVo;
import com.galaxyt.normae.file.service.MinIOService;
import com.galaxyt.normae.security.core.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传
 * @author zhouqi
 * @date 2020/8/24 16:12
 * @version v1.0.0
 * @Description
 *
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {


    @Autowired
    private MinIOService minIOService;

    /**
     * 上传
     * 要求前端传递过来的 body 中文件的 key 必须为 file
     * 实际开发中可以设置该接口的权限
     * @param file
     * @return
     */
    @Authority(isLogin = false, description = "文件上传")
    @PostMapping
    public GlobalResponseWrapper upload(@RequestPart("file") MultipartFile file) {

        log.debug("文件上传 start [{}]", file.getOriginalFilename());
        FileVo fileVo = this.minIOService.upload(file);
        log.debug("文件上传 end [{}]", fileVo);

        if (fileVo != null) {
            return new GlobalResponseWrapper().data(fileVo);
        } else {
            return new GlobalResponseWrapper(GlobalExceptionCode.FILE_UPLOAD_ERROR);
        }

    }


}
