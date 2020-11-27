package com.atguigu.edu.controller.front;

import com.atguigu.edu.entity.EduBanner;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduBannerService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/edu/front/")
public class FrontCourseController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduBannerService bannerService;

//    @Autowired
//    private RedisTemplate redisTemplate;

    //    @GetMapping("getAllBanner")
//    public RetVal getAllBanner(){
//        StringRedisSerializer keySerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(keySerializer);
//        List<EduBanner> bannerList = (List<EduBanner>)redisTemplate.opsForValue().get("indexInfo::banner");
//        if(bannerList==null){
//            bannerList = bannerService.list(null);
//            redisTemplate.opsForValue().set("indexInfo::banner",bannerList);
//        }
//        return RetVal.success().data("bannerList",bannerList);
//    }
    @Cacheable(value ="indexInfo",key ="'banner'")
    @GetMapping("getAllBanner")
    public RetVal getAllBanner(){
        List<EduBanner> bannerList = bannerService.list(null);
        return RetVal.success().data("bannerList",bannerList);
    }

    @Cacheable(value ="indexInfo",key ="'teacherCourse'")
    @GetMapping("queryTeacherAndCourse")
    public RetVal queryTeacherAndCourse(){
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(wrapper);

        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByAsc("sort");
        teacherWrapper.last("limit 8");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);
        return RetVal.success().data("courseList",courseList).data("teacherList",teacherList);
    }
}
