package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.mapper.EduTeacherMapper;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.request.TeacherConditionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-10-31
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //带条件的分页查询
    //传入一个分页对象和需要查询的字段对象
    @Override
    public void queryTeacherPageByCondition(Page<EduTeacher> teacherPage, TeacherConditionVO teacherConditionVO) {
        //创建一个查询的条件 wrapper
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        String teacherName = teacherConditionVO.getName();
        Integer level = teacherConditionVO.getLevel();
        String beginTime = teacherConditionVO.getBeginTime();
        String endTime = teacherConditionVO.getEndTime();
        //判断讲师名称是否为空
        //StringUtils 是String的一个工具类
        //isNotEmpty 判断是否为空
        if(StringUtils.isNotEmpty(teacherName)){
            //like表示模糊查询
            wrapper.like("name",teacherName);
        }
        if(level!=null){
            //eq 表示等于
            wrapper.eq("level",level);
        }
        if(StringUtils.isNotEmpty(beginTime)){
            //gt 大于大于
            wrapper.ge("gmt_create",beginTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            //le 小于等于
            wrapper.le("gmt_create",endTime);
        }
        //orderByDesc 表示升序
        wrapper.orderByDesc("gmt_create");
        //调用baseMapper的分页查询方法 第一个参数表示分页的大小 和分页的页码 第二个参数表示条件
        baseMapper.selectPage(teacherPage,wrapper);
    }

    @Override
    public Map<String, Object> queryTeacherPage(Page<EduTeacher> teacherPage) {
            baseMapper.selectPage(teacherPage,null);
            List<EduTeacher> teacherList = teacherPage.getRecords();
            long pages = teacherPage.getPages();
            long total = teacherPage.getTotal();
            long currentPage = teacherPage.getCurrent();
            boolean hasNext = teacherPage.hasNext();
            boolean hasPrevious = teacherPage.hasPrevious();
            long size = teacherPage.getSize();

            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("teacherList", teacherList);
            retMap.put("pages", pages);
            retMap.put("total", total);
            retMap.put("currentPage", currentPage);
            retMap.put("size", size);
            retMap.put("hasNext", hasNext);
            retMap.put("hasPrevious", hasPrevious);
            return retMap;
    }
}
