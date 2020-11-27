package com.atguigu.edu.controller.front;

import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.response.ChapterVO;
import com.atguigu.response.CourseDetailInfo;
import com.atguigu.response.RetVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/edu/front/course")
public class FrontIndexController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    //1.分页查询课程列表信息

    //2.课程详情信息查询
    @GetMapping("queryCourseDetailById/{courseId}")
    public RetVal queryCourseDetailById(@PathVariable String courseId){
        //a.编写课程详情代码
        CourseDetailInfo courseDetailInfoVo=courseService.queryCourseDetailById(courseId);
        //b.拿到章节小节信息
        List<ChapterVO> chapterAndSection = chapterService.getChapterAndSection(courseId);
        return RetVal.success().data("courseDetailInfoVo",courseDetailInfoVo).data("chapterAndSection",chapterAndSection);
    }
}