package com.galaxyt.normae.api.file.vo;

import lombok.Data;

/**
 * 文件上传成功之后的包装
 * @author zhouqi
 * @date 2020/5/29 14:22
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/29 14:22     zhouqi          v1.0.0           Created
 *
 */
@Data
public class FileVo {


    /**
     * 文件服务器地址
     */
    private String domain;


    /**
     * 文件上传之后在文件服务器上的相对路径
     * 前面拼接上 domain 即可访问
     */
    private String path;

}
