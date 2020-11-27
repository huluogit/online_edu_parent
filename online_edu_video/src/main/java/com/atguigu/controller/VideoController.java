package com.atguigu.controller;

import com.atguigu.response.RetVal;
import com.atguigu.service.VideoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/aliyun")
@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;

    //1.视频上传
    @PostMapping("uploadAliyunVideo")
    public RetVal uploadAliyunVideo(MultipartFile file){
        String videoId = videoService.uploadAliyunVideo(file);
        return RetVal.success().data("videoId",videoId);
    }
    //2.删除视频
    @DeleteMapping("{videoId}")
    public RetVal deleteSingleVideo(@PathVariable String videoId){
        videoService.deleteVideo(videoId);
        return RetVal.success();
    }
    //3.删除多个视频
    @DeleteMapping("deleteMultiVideo")
    public RetVal deleteMultiVideo(@RequestParam("videoIdList") List<String> videoIdList){
        String videoId = StringUtils.join(videoIdList, ",");
        videoService.deleteVideo(videoId);
        return RetVal.success();
    }

    //测试
    public static void main(String[] args) {
        List<String> videoIdList=new ArrayList<>();
        videoIdList.add("b7d9b85c8a204d81bb230f45ee4c3b2f");
        videoIdList.add("a7d9b85c8a204d81bb230f45ee4c3b2f");
        String join = StringUtils.join(videoIdList, ",");
        System.out.println(join);

    }
    //获取播放凭证
    @GetMapping("getVideoPlayAuth/{videoId}")
    public RetVal getVideoPlayAuth(@PathVariable String videoId){
        String playAuth=videoService.getVideoPlayAuth(videoId);
        return RetVal.success().data("playAuth",playAuth);
    }

}