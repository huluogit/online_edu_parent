package com.atguigu;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;

import java.util.List;

public class TestVideo {
    //账号AK信息请填写(必选)
    private static final String accessKeyId = "LTAI4GDqFiQoKvtknVjhioJ1";
    //账号AK信息请填写(必选)
    private static final String accessKeySecret = "uYkgl9Fcsc7xg3WWPgJhqLIYZIcK8z";

   public static void main(String[] args) throws ClientException {
//        //1.音视频上传-本地文件上传
//        //视频标题(必选)
//        String title = "测试标题";
//        //本地文件上传和文件流上传时，文件名称为上传文件绝对路径，如:/User/sample/文件名称.mp4 (必选)
//        //文件名必须包含扩展名
//        String fileName = "D:\\尚硅谷0621java班\\17_在线教育\\day10\\资料\\资料\\online.mp4";
//        //本地文件上传
//        testUploadVideo(accessKeyId, accessKeySecret, title, fileName);


//        getPlayInfo();
//        getVideoPlayAuthResponse();
          deleteVideo();
   }

    /**
     * 本地文件上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }

    /*获取播放地址函数*/
    public static void getPlayInfo() throws ClientException {
        DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId("e54755d86bfa4c4588e2c94aa36e5d68");
            response=client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("播放地址 = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
    /*获取播放凭证函数*/
    public static void getVideoPlayAuthResponse() throws ClientException {

        DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);

        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId("e54755d86bfa4c4588e2c94aa36e5d68");
            response =  client.getAcsResponse(request);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }


    /**
     * 删除视频
     * @param
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public static void deleteVideo() throws ClientException {
        DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
        DeleteVideoResponse response = new DeleteVideoResponse();
        try {
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds("5f1e4387546a490ca473f5e084253fcb,9403a0be834d4f16a683a95025912887");
            response =  client.getAcsResponse(request);
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
