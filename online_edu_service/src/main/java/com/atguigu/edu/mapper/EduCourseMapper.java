package com.atguigu.edu.mapper;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.response.CourseDetailInfo;
import com.atguigu.response.EduCourseConfirmVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-07
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    EduCourseConfirmVo getCourseConfirmInfo(String courseId);

    CourseDetailInfo queryCourseDetailById(String courseId);
}
