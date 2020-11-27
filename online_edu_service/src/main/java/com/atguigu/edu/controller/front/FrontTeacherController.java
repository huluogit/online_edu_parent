package com.atguigu.edu.controller.front;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/front/teacher")
@CrossOrigin
public class FrontTeacherController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService eduCourseService;
    @GetMapping("queryTeacherPage/{pageNum}/{pageSize}")
    public RetVal queryTeacherPage(@PathVariable("pageNum") long pageNum,
                                   @PathVariable("pageSize") long pageSize) {
        Page<EduTeacher> teacherPage = new Page<>(pageNum, pageSize);
        Map<String,Object> retMap=teacherService.queryTeacherPage(teacherPage);
        return RetVal.success().data(retMap);
    }

    @GetMapping("queryTeacherDetailById/{teacherId}")
    public RetVal queryTeacherDetailById(@PathVariable String teacherId) {
        //a.查询讲师基本信息
        EduTeacher teacher = teacherService.getById(teacherId);
        //b.讲师所授予的课程
        List<EduCourse> courseList=eduCourseService.queryCourseByTeacherId(teacherId);
        return RetVal.success().data("teacher",teacher).data("courseList",courseList);
    }
}
