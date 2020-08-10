package com.galaxyt.normae.file.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * FastDFS service
 * @author zhouqi
 * @date 2020/5/29 13:53
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2020/5/29 13:53     zhouqi          v1.0.0           Created
 *
 */
@Service
public class FastDFSService {


    @Autowired
    private FastFileStorageClient fastFileStorageClient;


    /**
     * 文件上传
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        try {
            StorePath storePath = this.fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
            return storePath.getFullPath();
        } catch (IOException e) {
            //ignore
            e.printStackTrace();
        } catch (Exception e) {
            //ignore
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    public String upload(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            StorePath storePath = this.fastFileStorageClient.uploadFile(inputStream, file.length(), FilenameUtils.getExtension(file.getName()), null);
            return storePath.getFullPath();
        } catch (FileNotFoundException e) {
            //ignore
            e.printStackTrace();
        } catch (Exception e) {
            //ignore
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件url
     * @return
     */
    public byte[] download(String fileUrl) {
        String pathurl = fileUrl.substring(fileUrl.indexOf("group1"));
        String group = pathurl.substring(0, pathurl.indexOf("/"));
        String path = pathurl.substring(pathurl.indexOf("/") + 1);
        byte[] bytes = this.fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
        return bytes;
    }



}
