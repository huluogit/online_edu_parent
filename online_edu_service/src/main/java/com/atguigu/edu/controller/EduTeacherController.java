package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.request.TeacherConditionVO;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-10-31
 */
/**
 * @RestController 开启rest风格
 * @RequestMapping("/edu/teacher") 表示前端访问的地址
 * @CrossOrigin 解决跨域问题
 */

@RestController
@RequestMapping("/edu/teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;


    //1.查询所有讲师
    @GetMapping
    public RetVal getAllTeacher(){
        List<EduTeacher> teacherList = teacherService.list(null);
//        try {
//            int a=1/0;
//        } catch (Exception e) {
//            throw new EduException();
//        }
        for (EduTeacher teacher : teacherList) {
            System.out.print(teacher.getName());
            System.out.print(teacher.getGmtCreate());
            System.out.println(teacher.getGmtModified());
        }
        //返回给前端 success表示成功
        //data 代表写给前端的参数
        //第一个参数 是前端获取数据的名字
        //第二个是后端还回的数据集合
        return RetVal.success().data("teacherList",teacherList);
    }

    //2.删除讲师
    //"{id}" 表是传入的参数
    @DeleteMapping("{id}")
    public RetVal deleteTeacherById(@ApiParam(name = "id",value = "讲师id",required = true) @PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }

    //3.讲师列表分页查询
    /*
    * pageNum:表示分页页码
    *
    * pageSize：表示分页大小
    * */
    @GetMapping("queryTeacherPage/{pageNum}/{pageSize}")
    public RetVal queryTeacherPage(
            @PathVariable("pageNum") long pageNum,
            @PathVariable("pageSize") long pageSize){
        //创建一个分页对象 传入当前页码pageNum 和 每页页码的大小pageSize
        Page<EduTeacher> teacherPage = new Page<>(pageNum, pageSize);
        //表示无条件的分页查询 所以wrapper为 null
        teacherService.page(teacherPage, null);
        //getTotal()方法表示总记录数
        long total = teacherPage.getTotal();
        //getRecords()方法表示获取分页的总结果集
        List<EduTeacher> teacherList = teacherPage.getRecords();
        //返回给前端总页数和查询到所有老师的集合
        return RetVal.success().data("total",total).data("teacherList",teacherList);
    }

    //4.讲师列表分页查询带条件
    @GetMapping("queryTeacherPageByCondition/{pageNum}/{pageSize}")
    public RetVal queryTeacherPageByCondition(
            //分页当前页码
            @PathVariable("pageNum") long pageNum,
            //分页大小
            @PathVariable("pageSize") long pageSize,
            //查询的条件 因为本身的javabean字段太多 查询的字段只是其中几个 所有另外封装一个类
            TeacherConditionVO teacherConditionVo){
        //new 一个分页对象
        Page<EduTeacher> teacherPage = new Page<>(pageNum, pageSize);
        //调用teacherService层的根据查询条件分页的方法进行查询
        //第一个参数 分页对象 包括分页的大小和当前页
        //第二个参数 查询的条件
        teacherService.queryTeacherPageByCondition(teacherPage, teacherConditionVo);
        //总记录数
        long total = teacherPage.getTotal();
        //总的结果集
        List<EduTeacher> teacherList = teacherPage.getRecords();
        return RetVal.success().data("total",total).data("teacherList",teacherList);
    }
    //4.添加讲师
    @PostMapping
    //插入需要添加的对象值
    public RetVal saveTeacher(EduTeacher teacher){
        //返回一个boolean值看是否添加成功
        boolean flag = teacherService.save(teacher);
        if(flag){
            //成功 返回success
            return RetVal.success();
        }else{
            //失败 还回error
            return RetVal.error();
        }
    }
    //5.修改讲师
    @PutMapping
    public RetVal updateTeacher(EduTeacher teacher){
        boolean flag = teacherService.updateById(teacher);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //6.根据id查询讲师
    @GetMapping("{id}")
    public RetVal queryTeacherById(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return RetVal.success().data("teacher",teacher);
    }

    public static void main(String[] args) {
        ArrayList<String> list=new ArrayList<String>();
        list.add("111");
        list.add("222");
        list.add("333");
        for(Iterator<String> iterator = list.iterator(); iterator.hasNext();){
            String ele=iterator.next();
            if(ele.equals("111")) //（1）处
                list.remove("222"); //(2)处
        }
        System.out.println(list);
    }
}

