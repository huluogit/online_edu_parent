package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.request.CourseCondition;
import com.atguigu.request.CourseInfoVO;
import com.atguigu.response.EduCourseConfirmVo;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/edu/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;
    //1.添加课程信息
    @PostMapping("saveCourseInfo")
    public RetVal saveCourseInfo(CourseInfoVO courseInfoVO){
        String courseId = courseService.saveCourseInfo(courseInfoVO);
        return RetVal.success().data("courseId",courseId);
    }

    //3.分页查询课程信息带条件
    @GetMapping("queryCoursePageByCondition/{pageNum}/{pageSize}")
    public RetVal queryCoursePageByCondition(
            @PathVariable("pageNum") long pageNum,
            @PathVariable("pageSize") long pageSize,
            CourseCondition courseCondition) {
        Page<EduCourse> coursePage = new Page<>(pageNum, pageSize);
        courseService.queryCoursePageByCondition(coursePage, courseCondition);
        long total = coursePage.getTotal();
        List<EduCourse> courseList = coursePage.getRecords();
        return RetVal.success().data("total", total).data("courseList", courseList);
    }

    //4.根据课程id查询课程的信息
    @GetMapping("{id}")
    public RetVal getCourseById(@PathVariable String id){
        CourseInfoVO courseInfoVO=courseService.getCourseById(id);
        return RetVal.success().data("courseInfoVO",courseInfoVO);
    }

    //5.更新课程信息
    @PutMapping("updateCourseInfo")
    public RetVal updateCourseInfo(CourseInfoVO courseInfoVO){
        courseService.updateCourseInfo(courseInfoVO);
        return RetVal.success();
    }
    //6.课程发布确认
    @GetMapping("getCourseConfirmInfo/{courseId}")
    public RetVal getCourseConfirmInfo(@PathVariable String courseId){
        EduCourseConfirmVo courseConfirmVo=courseService.getCourseConfirmInfo(courseId);
        return RetVal.success().data("courseConfirm",courseConfirmVo);
    }
    //7.课程发布后端
    @GetMapping("publishCourse/{courseId}")
    public RetVal publishCourse(@PathVariable String courseId){
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus("Normal");
        courseService.updateById(course);
        return RetVal.success();
    }

    //8.删除课程
    @DeleteMapping("{courseId}")
    public RetVal deleteCourse(@PathVariable String courseId){
        courseService.deleteCourse(courseId);
        return RetVal.success();
    }

}

