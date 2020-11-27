package com.atguigu.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.Utils.VideoUtils;
import com.atguigu.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Override
    public String uploadAliyunVideo(MultipartFile file) {
        String videoId=null;
        try {
            //fileName=online.mp4
            String fileName = file.getOriginalFilename();
            //title =online
            String title=fileName.substring(0,fileName.indexOf("."));
            InputStream inputStream = file.getInputStream();
            //得到一个网络流的请求
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            if(response.isSuccess()){
                videoId = response.getVideoId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoId;

    }

    @Override
    public void deleteVideo(String videoId) {
        try{
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getVideoPlayAuth(String videoId) {
        try {
            DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            response = client.getAcsResponse(request);
            //播放凭证
            return response.getPlayAuth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
