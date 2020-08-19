package com.galaxyt.normae.file.web;

import com.galaxyt.normae.api.file.vo.FileVo;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import com.galaxyt.normae.file.service.MinIOService;
import com.galaxyt.normae.security.core.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/29 13:51
 * @Description
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/29 13:51     zhouqi          v1.0.0           Created
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
     *
     * @param file
     * @return
     */
    @Authority(mark = "upload", name = "图片文件上传", isLogin = false)
    @PostMapping
    public GlobalResponseWrapper upload(@RequestPart("file") MultipartFile file) {

        FileVo fileVo = this.minIOService.upload(file);

        if (fileVo != null) {
            return new GlobalResponseWrapper().data(fileVo);
        } else {
            return new GlobalResponseWrapper(GlobalExceptionCode.FILE_UPLOAD_ERROR);
        }

    }


}
