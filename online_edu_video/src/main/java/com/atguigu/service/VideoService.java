package com.atguigu.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    //上传视频
    String uploadAliyunVideo(MultipartFile file);

    //删除视频
    void deleteVideo(String videoId);

    //获取播放凭证
    String getVideoPlayAuth(String videoId);
}
