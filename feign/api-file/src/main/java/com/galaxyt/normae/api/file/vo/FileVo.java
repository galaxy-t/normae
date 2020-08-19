package com.galaxyt.normae.api.file.vo;

import lombok.Data;

/**
 * 上传文件信息表 vo
 * @Author guomiaomiao
 * @Date 2020/7/24 17:50
 * @Version 1.0
 */
@Data
public class FileVo {


    /**
     * 文件服务器地址
     */
    private String domain;


    /**
     * 文件原名称
     */
    private String originalFilename;

    /**
     * 文件上传之后在文件服务器上的名称
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private String extensionName;

    /**
     * 文件可以访问的 url 地址
     */
    private String url;

}
