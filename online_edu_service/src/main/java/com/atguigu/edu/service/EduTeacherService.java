package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.request.TeacherConditionVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-10-31
 */
public interface EduTeacherService extends IService<EduTeacher> {
    //带条件的分页查询
    void queryTeacherPageByCondition(Page<EduTeacher> teacherPage, TeacherConditionVO teacherConditionVO);

    Map<String, Object> queryTeacherPage(Page<EduTeacher> teacherPage);
}
