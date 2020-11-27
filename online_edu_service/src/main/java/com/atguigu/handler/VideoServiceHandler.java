package com.atguigu.handler;

import com.atguigu.edu.service.VideoServiceFeign;
import com.atguigu.response.RetVal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoServiceHandler implements VideoServiceFeign {
    @Override
    public RetVal deleteSingleVideo(String videoId) {
        return RetVal.success().message("返回兜底数据");
    }

    @Override
    public RetVal deleteMultiVideo(List<String> videoIdList) {
        return RetVal.success().message("返回兜底数据");
    }
}
