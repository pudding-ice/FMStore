package com.myjava.core.controller;
import javax.servlet.http.HttpServletRequest;

import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 遗留问题:
 * 33行  如果改为  private String conf = this.getClass().getResource("/").getPath();
 * 预期的conf的值应该是当前this(FastDFSController实例对象)所在的项目目录(web_shop模块)的classpath,
 * 但是实际的打印却跑到了interface模块,查看项目结构和模块输出目录都没有查询到问题,这个暂时未解决
 */

@RestController
@RequestMapping("/fdfs")
public class FastDFSController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    private String conf = this.getClass().getResource("/FastDFS/fdfs_client.conf").getPath();

    public void setFILE_SERVER_URL(String file_server_url) {
        this.FILE_SERVER_URL = file_server_url;
    }

    @RequestMapping("/upload")
    public ResultMessage uploadImage(MultipartFile file) {
        FastDFSClient client = null;
        String filePath = null;
        try {
            client = new FastDFSClient(conf);
            String path = client.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            filePath = FILE_SERVER_URL + path;
            return new ResultMessage(true, filePath);
        } catch (Exception e) {
            return new ResultMessage(false, "上传失败");
        }
    }

    @RequestMapping("/deleteImg")
    public ResultMessage deleteImg(String url) throws Exception {
        String path = url.substring(FILE_SERVER_URL.length());
        FastDFSClient client = new FastDFSClient(conf);
        Integer integer = client.delete_file(path);
        if (integer < 0) {
            return new ResultMessage(false, "删除失败");
        } else {
            return new ResultMessage(true, "删除成功");
        }
    }

    @RequestMapping("/uploadUeImage")
    public Map uploadUeImage(MultipartHttpServletRequest request) throws Exception {
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        Map<String, MultipartFile> map = multiFileMap.toSingleValueMap();
        MultipartFile ueFile = map.get("upfile");
        FastDFSClient fastDFS = new FastDFSClient(conf);
        //上传文件返回文件保存的路径和文件名
        String path = fastDFS.uploadFile(ueFile.getBytes(), ueFile.getOriginalFilename(), ueFile.getSize());
        //拼接上服务器的地址返回给前端
        String url = FILE_SERVER_URL + path;
        Map<String, Object> result = new HashMap<>();
        result.put("state", "SUCCESS");
        result.put("url", url);
        result.put("title", ueFile.getOriginalFilename());
        result.put("original", ueFile.getOriginalFilename());
        return result;
    }
}
