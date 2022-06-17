package com.myjava.core.controller;

import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fdfs")
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/upload")
    public ResultMessage uploadImage(MultipartFile file) {
        FastDFSClient client = null;
        String filePath = null;
        try {
            String conf = this.getClass().getResource("/FastDFS/fdfs_client.conf").getPath();
            client = new FastDFSClient(conf);
            String path = client.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            filePath = FILE_SERVER_URL + path;
            return new ResultMessage(true, filePath);
        } catch (Exception e) {
            return new ResultMessage(false, "上传失败");
        }
    }

    public void setFILE_SERVER_URL(String file_server_url) {
        this.FILE_SERVER_URL = file_server_url;
    }
}
