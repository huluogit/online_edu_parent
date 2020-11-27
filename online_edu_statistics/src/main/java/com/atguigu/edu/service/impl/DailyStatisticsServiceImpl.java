package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.DailyStatistics;
import com.atguigu.edu.mapper.DailyStatisticsMapper;
import com.atguigu.edu.service.DailyStatisticsService;
import com.atguigu.edu.service.UserServiceFeign;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-12
 */
@Service
public class DailyStatisticsServiceImpl extends ServiceImpl<DailyStatisticsMapper, DailyStatistics> implements DailyStatisticsService {
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Override
    public void generateData(String day) {
        RetVal retVal = userServiceFeign.queryRegisterNum(day);
        DailyStatistics dailyStatistics = new DailyStatistics();
       dailyStatistics.setDateCalculated(day);
        Integer registerNum = (Integer)retVal.getData().get("registerNum");
        //来源于user微服务
       dailyStatistics.setRegisterNum(registerNum);
       dailyStatistics.setCourseNum(RandomUtils.nextInt(300,400));
       dailyStatistics.setVideoViewNum(RandomUtils.nextInt(400,600));
       dailyStatistics.setLoginNum(RandomUtils.nextInt(200,400));
       baseMapper.insert(dailyStatistics);
    }

    @Override
    public Map<String, Object> showStatistics(String dataType, String beginTime, String endTime) {
        QueryWrapper<DailyStatistics> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", beginTime, endTime);
        List<DailyStatistics> dailyStatistics = baseMapper.selectList(wrapper);
        //x轴信息(日期) y轴信息(日期所对应的数据)
        List<String> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();
        for (DailyStatistics dailyStatistic : dailyStatistics) {
            String singleDay = dailyStatistic.getDateCalculated();
            xData.add(singleDay);
            switch (dataType) {
                case "register_num":
                    Integer registerNum = dailyStatistic.getRegisterNum();
                    yData.add(registerNum);
                    break;
                case "login_num":
                    Integer loginNum = dailyStatistic.getLoginNum();
                    yData.add(loginNum);
                    break;
                case "video_view_num":
                    Integer videoViewNum = dailyStatistic.getVideoViewNum();
                    yData.add(videoViewNum);
                    break;
                case "course_num":
                    Integer courseNum = dailyStatistic.getCourseNum();
                    yData.add(courseNum);
                    break;
                default:
                    break;
            }
        }
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("xData",xData);
        retMap.put("yData",yData);
        return retMap;
    }
}
