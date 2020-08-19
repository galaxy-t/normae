package com.galaxyt.normae.api.file;

import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * FIle 服务 Feign 客户端
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/28 16:03
 * @Description //
 * Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/5/28 16:03     zhouqi          v1.0.0           Created
 */
@Primary
@FeignClient(value = "file", fallbackFactory = FileFallback.class)
public interface FileClient {

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    GlobalResponseWrapper upload(@RequestPart("file") MultipartFile file);

}
