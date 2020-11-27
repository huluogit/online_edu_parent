package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.request.CourseCondition;
import com.atguigu.request.CourseInfoVO;
import com.atguigu.response.CourseDetailInfo;
import com.atguigu.response.EduCourseConfirmVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-07
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVO courseInfoVO);

    void queryCoursePageByCondition(Page<EduCourse> coursePage, CourseCondition courseCondition);

    CourseInfoVO getCourseById(String id);

    void updateCourseInfo(CourseInfoVO courseInfoVO);

    EduCourseConfirmVo getCourseConfirmInfo(String courseId);

    void deleteCourse(String courseId);

    List<EduCourse> queryCourseByTeacherId(String teacherId);

    CourseDetailInfo queryCourseDetailById(String courseId);
}
