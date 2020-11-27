package com.atguigu.service.impl;

import com.atguigu.oss.OssTemplate;
import com.atguigu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OssTemplate ossTemplate;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        //得到文件原始名称 aa.jpg
        String originalFilename = file.getOriginalFilename();
        //得到一个随机文件名称
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        //得到文件的后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        fileName=fileName+suffix;
        InputStream inputStream = file.getInputStream();
        String retUrl = ossTemplate.uploadFile(fileName, inputStream);
        return retUrl;
    }

    @Override
    public boolean deleteFile(String fileName) {
        ossTemplate.deleteSingleFile(fileName);
        return true;
    }
}
