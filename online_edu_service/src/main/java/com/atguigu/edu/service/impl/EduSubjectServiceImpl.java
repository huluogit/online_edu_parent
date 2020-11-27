package com.atguigu.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.entity.ExcelSubject;
import com.atguigu.edu.mapper.EduSubjectMapper;
import com.atguigu.edu.service.EduSubjectService;
import com.atguigu.exception.EduException;
import com.atguigu.listener.MySubjectListener;
import com.atguigu.response.SubjectVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-11-06
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Autowired
    private MySubjectListener mySubjectListener;

    //上传文件 MultipartFile 上传的文件对象
    @Override
    public void uploadSubject(MultipartFile file) throws Exception {
        //上传文件需要依靠输入流
        InputStream inputStream = file.getInputStream();
        //调用阿里巴巴EasyExcel类的read(读取方法)
        //第一个参数 传入一个输入流
        //第二个参数  表示写入数据的对象类型
        //第三个参数  传入了继承了阿里巴巴规定AnalysisEventListener 的对象
        //doReadAll() :表示全部读取
        EasyExcel.read(inputStream, ExcelSubject.class,mySubjectListener).doReadAll();
    }
    //判断条件是标题与parentId不同
    @Override
    public EduSubject existSubject(String title, String parentId){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",parentId);
        wrapper.eq("title",title);
        //通过查询parent_id和title判断这个对象是否存在
        EduSubject existSubject = baseMapper.selectOne(wrapper);
        return existSubject;
    }

    @Override
    public List<SubjectVo> getAllSubject() {
        //a.设计一个返回对象给前端
        //b.查询数据库返回所有课程分类
        List<EduSubject> allSubject = baseMapper.selectList(null);
        //c.先查询所有的一级分类(找组长)
        List<SubjectVo> parentSubjectVos = new ArrayList<>();
        for (EduSubject subject : allSubject) {
            //判断标准	parentId=0
            if(subject.getParentId().equals("0")){
                SubjectVo parentSubjectVo = new SubjectVo();
                //把对应属性值赋值给响应对象
                BeanUtils.copyProperties(subject,parentSubjectVo);
                parentSubjectVos.add(parentSubjectVo);
            }
        }
        //d.把一级分类放到一个角落(map)
        Map<String, SubjectVo> parentSubjectMap = new HashMap<>();
        for (SubjectVo parentSubjectVo : parentSubjectVos) {
            //key一级分类的id value一级分类对象
            parentSubjectMap.put(parentSubjectVo.getId(),parentSubjectVo);
        }
        //e.查询所有的二级分类(找组员)
        for (EduSubject subject : allSubject) {
            //判断标准	parentId！=0
            if(!subject.getParentId().equals("0")){
                //拿到二级分类的parentId(组号) 从map中找一级分类
                SubjectVo parentSubjectVo = parentSubjectMap.get(subject.getParentId());
                //一级分类里面有个children属性 把二级分类放到children里面 成为它的子节点
                SubjectVo childSubjectVo = new SubjectVo();
                //把对应属性值赋值给响应对象
                BeanUtils.copyProperties(subject,childSubjectVo);
                parentSubjectVo.getChildren().add(childSubjectVo);
            }
        }
        //f.返回所有的一级分类
        return parentSubjectVos;
    }

    @Override
    public boolean deleteSubjectById(String id) {
        //拿到这个id 查询该节点是否有子节点
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        //说明没有子节点可以删除
        if(count==0){
            int rows = baseMapper.deleteById(id);
            return rows>0;
        }else{
            throw new EduException(20001,"该节点存在子节点");
        }
    }

    @Override
    public boolean saveParentSubject(EduSubject subject) {
        EduSubject parentSubject = existSubject(subject.getTitle(), "0");
        if(parentSubject==null){
            parentSubject = new EduSubject();
            parentSubject.setTitle(subject.getTitle());
            parentSubject.setParentId("0");
            int rows = baseMapper.insert(parentSubject);
            return rows>0;
        }
        return false;
    }

    @Override
    public boolean saveChildSubject(EduSubject subject) {
        EduSubject existSubject = existSubject(subject.getTitle(), subject.getParentId());
        if(existSubject==null){
            int rows = baseMapper.insert(subject);
            return rows>0;
        }
        return false;
    }
}
