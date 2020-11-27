package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduChapter;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.response.ChapterVO;
import com.atguigu.response.RetVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-09
 */
@RestController
@RequestMapping("/edu/chapter")
@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;
    //1.查询所有的章节和小节
    @GetMapping("getChapterAndSection/{courseId}")
    public RetVal getChapterAndSection(@PathVariable String courseId){
        List<ChapterVO> chapterSectionList=chapterService.getChapterAndSection(courseId);
        return RetVal.success().data("chapterSectionList",chapterSectionList);
    }

    //2.添加章节
    @PostMapping
    public RetVal saveChapter(EduChapter chapter){
        boolean flag=chapterService.saveChapter(chapter);
        if(flag){
            return RetVal.success();
        }else {
            return RetVal.error();
        }
    }
    //3.根据章节id查询章节信息
    @GetMapping("getChapter/{id}")
    public RetVal getChapter(@PathVariable String id){
        EduChapter chapter = chapterService.getById(id);
        return RetVal.success().data("chapter",chapter);
    }
    //4.更新章节信息
    @PutMapping
    public RetVal updateChapter(EduChapter chapter){
        boolean flag = chapterService.updateById(chapter);
        if(flag){
            return RetVal.success();
        }else {
            return RetVal.error();
        }
    }
    //5.删除章节信息
    @DeleteMapping("{chapterId}")
    public RetVal deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if(flag){
            return RetVal.success();
        }else {
            return RetVal.error();
        }
    }
}

