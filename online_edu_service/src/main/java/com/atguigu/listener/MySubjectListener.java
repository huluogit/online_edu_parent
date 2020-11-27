package com.atguigu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.entity.ExcelSubject;
import com.atguigu.edu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Excel表格的读取和写入数据
//@Component 将这个类加入到spring容器 交给spring容器进行管理
//ExcelSubject 表示读取和写入的数据类型
@Component
public class MySubjectListener extends AnalysisEventListener<ExcelSubject> {
    //自动注入
    @Autowired
    private EduSubjectService subjectService;

    //一行一行的读数据
    @Override
    public void invoke(ExcelSubject excelSubject, AnalysisContext analysisContext) {
        //读取到的数据分为两列 一级分类  二级分类
        String parentSubjectName = excelSubject.getParentSubjectName();
        //保存一级分离 需要判断DB是否存在 parentId=0
        EduSubject parentSubject = subjectService.existSubject(parentSubjectName, "0");
        if(parentSubject==null){
            parentSubject = new EduSubject();
            parentSubject.setTitle(parentSubjectName);
            parentSubject.setParentId("0");
            subjectService.save(parentSubject);
        }
        //保存二级分离 需要判断DB是否存在 parentId=一级分类的id
        String parentSubejctId = parentSubject.getId();
        String childSubjectName = excelSubject.getChildSubjectName();
        EduSubject childSubject = subjectService.existSubject(childSubjectName, parentSubejctId);
        if(childSubject==null){
            childSubject = new EduSubject();
            childSubject.setTitle(childSubjectName);
            childSubject.setParentId(parentSubejctId);
            subjectService.save(childSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
