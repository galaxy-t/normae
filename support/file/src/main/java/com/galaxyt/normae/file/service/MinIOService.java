package com.galaxyt.normae.file.service;

import com.galaxyt.normae.api.file.vo.FileVo;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 *
 * @author zhouqi
 * @date 2020/8/19 16:04
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/8/19 16:04     zhouqi          v1.0.0           Created
 *
 */
@Service
public class MinIOService {


    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    @Value("${minio.bucket-name}")
    private String bucketName;


    private MinioClient minioClient;

    public MinIOService() {
        this.minioClient = MinioClient.builder().endpoint(this.endpoint).credentials(this.username, this.password).build();
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    public FileVo upload(MultipartFile file) {

        //文件服务器地址
        String domain = String.format("%s/%s", this.endpoint, this.bucketName);
        //生成一个新的 uuid
        String uuid = UUID.randomUUID().toString();
        //文件的原始名称
        String originalFilename = file.getOriginalFilename();
        //文件扩展名
        String extensionName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //文件上传到文件服务器的名称
        String fileName = String.format("%s%s", uuid, extensionName);
        //可以访问的 url
        String url = String.format("%s/%s", domain, fileName);

        FileVo vo = new FileVo();
        vo.setDomain(domain);
        vo.setOriginalFilename(originalFilename);
        vo.setFileName(fileName);
        vo.setExtensionName(extensionName);
        vo.setUrl(url);

        try {
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder().bucket(this.bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .build());
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }









}
