package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduCourseDescription;
import com.atguigu.edu.mapper.EduCourseMapper;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseDescriptionService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.exception.EduException;
import com.atguigu.request.CourseCondition;
import com.atguigu.request.CourseInfoVO;
import com.atguigu.response.CourseDetailInfo;
import com.atguigu.response.EduCourseConfirmVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-07
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduSectionService sectionService;

    @Override
    public String saveCourseInfo(CourseInfoVO courseInfoVO) {
        //c.保存课程基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO,course);
        baseMapper.insert(course);
        //d.保存课程描述信息
        EduCourseDescription description = new EduCourseDescription();
        //e.它们两公用一个主键id
        description.setId(course.getId());
        description.setDescription(courseInfoVO.getDescription());
        descriptionService.save(description);
        return course.getId();
    }

    @Override
    public void queryCoursePageByCondition(Page<EduCourse> coursePage, CourseCondition courseCondition) {
        //获取每个查询参数
        String title = courseCondition.getTitle();
        String status = courseCondition.getStatus();
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断以上传递过来的参数是否为空
        if(StringUtils.isNotEmpty(title)){
            wrapper.like("title",title);
        }

        if(StringUtils.isNotEmpty(status)){
            wrapper.ge("status",status);
        }
        baseMapper.selectPage(coursePage,wrapper);
    }

    @Override
    public CourseInfoVO getCourseById(String id) {
        CourseInfoVO courseInfoVO=new CourseInfoVO();
        //查询课程基本信息
        EduCourse course = baseMapper.selectById(id);
        BeanUtils.copyProperties(course,courseInfoVO);
        //查询课程描述信息
        EduCourseDescription courseDescription = descriptionService.getById(id);
        if(courseDescription!=null){
            courseInfoVO.setDescription(courseDescription.getDescription());
        }
        return courseInfoVO;
    }

    @Override
    public void updateCourseInfo(CourseInfoVO courseInfoVO) {
        //c.保存课程基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVO,course);
        baseMapper.updateById(course);
        //d.保存课程描述信息
        EduCourseDescription description = new EduCourseDescription();
        //e.它们两公用一个主键id
        description.setId(course.getId());
        description.setDescription(courseInfoVO.getDescription());
        descriptionService.updateById(description);
    }

    @Override
    public EduCourseConfirmVo getCourseConfirmInfo(String courseId) {
        return baseMapper.getCourseConfirmInfo(courseId);
    }

    @Override
    public void deleteCourse(String courseId) {
        //1.删除该课程的章节
        chapterService.deleteChapterByCourseId(courseId);

        //2.删除该课程的小节
        sectionService.deleteSectionByCourseId(courseId);
        //3.删除该课程
        int rows = baseMapper.deleteById(courseId);
        if(rows==0){
            //这个异常是运行时异常
            throw new EduException(20001,"删除课程信息失败");
        }
        //4.删除该课程的课程描述信息
        descriptionService.removeById(courseId);
    }

    @Override
    public List<EduCourse> queryCourseByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public CourseDetailInfo queryCourseDetailById(String courseId) {
        return baseMapper.queryCourseDetailById(courseId);
    }
}
