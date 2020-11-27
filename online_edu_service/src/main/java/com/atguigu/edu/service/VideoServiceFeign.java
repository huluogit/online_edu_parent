package com.atguigu.edu.service;

import com.atguigu.handler.VideoServiceHandler;
import com.atguigu.response.RetVal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(value = "EDU-VIDEO",fallback = VideoServiceHandler.class)
public interface VideoServiceFeign {
    //2.删除视频
    @DeleteMapping("/aliyun/{videoId}")
    public RetVal deleteSingleVideo(@PathVariable("videoId") String videoId);
    //3.删除多个视频
    @DeleteMapping("/aliyun/deleteMultiVideo")
    public RetVal deleteMultiVideo(@RequestParam("videoIdList") List<String> videoIdList);
}
