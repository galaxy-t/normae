package com.galaxyt.normae.file.web;

import com.galaxyt.normae.api.file.vo.FileVo;
import com.galaxyt.normae.core.annotation.NotWrapper;
import com.galaxyt.normae.core.exception.GlobalExceptionCode;
import com.galaxyt.normae.core.wrapper.GlobalResponseWrapper;
import com.galaxyt.normae.file.service.FastDFSService;
import com.galaxyt.normae.file.vo.FileDetailVo;
import com.galaxyt.normae.security.core.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2020/5/29 13:51
 * @Description //
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
    private FastDFSService fastDFSService;

    @Value("${fdfs.url-prefix}")
    private String urlPrefix;

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

        String fullPath = this.fastDFSService.upload(file);

        if (fullPath == null) {
            return new GlobalResponseWrapper(GlobalExceptionCode.FILE_UPLOAD_FAIL);
        }

        FileVo fileVo = new FileVo();
        fileVo.setDomain(this.urlPrefix);
        fileVo.setPath(fullPath);

        return new GlobalResponseWrapper().data(fileVo);
    }

    /**
     * 上传
     * 要求前端传递过来的 body 中文件的 key 必须为 file
     *
     * @param file
     * @return
     */
    @Authority(mark = "uploadFile", name = "前端调用文件上传", isLogin = false)
    @PostMapping("/upload-file")
    public GlobalResponseWrapper uploadFile(@RequestPart("file") MultipartFile file) {

        String fullPath = this.fastDFSService.upload(file);

        if (fullPath == null) {
            return new GlobalResponseWrapper(GlobalExceptionCode.FILE_UPLOAD_FAIL);
        }

        FileDetailVo vo = new FileDetailVo();
        vo.setDomain(this.urlPrefix);
        vo.setPath(fullPath);
        vo.setAbsolutePath(this.urlPrefix + fullPath);
        vo.setOriName(file.getOriginalFilename());
        if (file.getOriginalFilename().length() > 1) {
            vo.setExtName(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        }

        return new GlobalResponseWrapper().data(vo);
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件路径
     * @return ResponseEntity<byte[]>
     */
    @Authority(mark = "download", name = "下载文件", isLogin = false)
    @PostMapping("/download")
    @NotWrapper
    public ResponseEntity<byte[]> download(@RequestParam("fileUrl") String fileUrl) {
        ResponseEntity<byte[]> entity = null;
        byte[] bytes = this.fastDFSService.download(fileUrl);
        entity = new ResponseEntity<>(bytes, HttpStatus.OK);
        return entity;
    }

}
