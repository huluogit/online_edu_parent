package com.atguigu.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String uploadFile(MultipartFile file) throws Exception;

    boolean deleteFile(String fileName);
}
