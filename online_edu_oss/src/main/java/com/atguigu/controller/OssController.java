package com.atguigu.controller;

import com.atguigu.response.RetVal;
import com.atguigu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    //1.文件上传
    @PostMapping("uploadFile")
    public RetVal uploadFile(MultipartFile file) throws Exception{
        String retUrl=ossService.uploadFile(file);
        return RetVal.success().data("retUrl",retUrl);
    }
    //2.文件删除
    @PostMapping("deleteFile")
    public RetVal deleteFile(String fileName) {
        boolean flag=ossService.deleteFile(fileName);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
}
