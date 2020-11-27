package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduSubject;
import com.atguigu.response.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-06
 */
public interface EduSubjectService extends IService<EduSubject> {
    //上传文件
    void uploadSubject(MultipartFile file) throws Exception;

    //判断条件是标题与parentId不同
    EduSubject existSubject(String title, String parentId);

    //获取所有课程
    List<SubjectVo> getAllSubject();

    //按照Id删除课程
    boolean deleteSubjectById(String id);

    //添加课程的一级分类
    boolean saveParentSubject(EduSubject subject);

    //添加具体的二级分类课程
    boolean saveChildSubject(EduSubject subject);
}
