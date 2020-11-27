package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduSection;
import com.atguigu.edu.mapper.EduSectionMapper;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.edu.service.VideoServiceFeign;
import com.atguigu.exception.EduException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程小节 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-09
 */
@Transactional
@Service
public class EduSectionServiceImpl extends ServiceImpl<EduSectionMapper, EduSection> implements EduSectionService {
    @Autowired
    private VideoServiceFeign videoServiceFeign;
    @Override
    public void addSection(EduSection section) {
        //1.判断是否存在小节
        QueryWrapper<EduSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",section.getCourseId());
        queryWrapper.eq("chapter_id",section.getChapterId());
        queryWrapper.eq("title",section.getTitle());
        EduSection existSection = baseMapper.selectOne(queryWrapper);
        if(existSection==null){
            baseMapper.insert(section);
        }else{
            throw new EduException(20001,"存在重复的小节");
        }
    }

    @Override
    public void deleteSection(String id) {
        //1.通过小节id查询小节信息
        EduSection section = baseMapper.selectById(id);
        //2.通过小节得到videoId
        String videoSourceId = section.getVideoSourceId();
        if(StringUtils.isNotEmpty(videoSourceId)){
            //3.需调用远程RPC的video微服务 需要提供一个videoId
            videoServiceFeign.deleteSingleVideo(videoSourceId);
        }
        //TODO 删除视频
        baseMapper.deleteById(id);
    }

    @Override
    public void deleteSectionByCourseId(String courseId) {
        //a.通过课程id查询该课程所有的小节信息
        QueryWrapper<EduSection> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduSection> sectionList = baseMapper.selectList(wrapper);
        //b.迭代所有的小节 所有小节的视频id拿到(封装成一个视频idList)
        List<String> videoIdList = new ArrayList<>();
            for (EduSection section : sectionList) {
                String videoSourceId = section.getVideoSourceId();
                if (StringUtils.isNotEmpty(videoSourceId)) {
                    videoIdList.add(videoSourceId);
                }
        }
        //TODO c.通过RPC的形式调用video微服务
        if(videoIdList.size() != 0) {
            videoServiceFeign.deleteMultiVideo(videoIdList);
        }
        //d.根据课程id删除该课程对应的小节
        baseMapper.delete(wrapper);
    }
}
