package com.galaxyt.normae.file.vo;

import lombok.Data;

/**
 * 上传文件信息表 vo
 * @Author guomiaomiao
 * @Date 2020/7/24 17:50
 * @Version 1.0
 */
@Data
public class FileDetailVo {


    /**
     * 文件服务器地址
     */
    private String domain;


    /**
     * 文件上传之后在文件服务器上的相对路径
     * 前面拼接上 domain 即可访问
     */
    private String path;

    /**
     * 数据库存储路径
     */
    private String absolutePath;

    /**
     * 文件名称
     */
    private String oriName;

    /**
     * 文件扩展名
     */
    private String extName;

}
