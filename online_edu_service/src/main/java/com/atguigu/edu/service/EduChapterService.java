package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduChapter;
import com.atguigu.response.ChapterVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-09
 */
public interface EduChapterService extends IService<EduChapter> {

    boolean saveChapter(EduChapter chapter);

    boolean deleteChapter(String chapterId);

    List<ChapterVO> getChapterAndSection(String courseId);

    void deleteChapterByCourseId(String courseId);
}
