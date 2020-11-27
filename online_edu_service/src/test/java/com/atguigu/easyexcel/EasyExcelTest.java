package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTest {
    //1.往excel中写数据
    @Test
    public void testWriteExcel(){
        //a.往哪个文件当中写
        String filePath="C:\\200621\\work\\one.xlsx";
        //b.写什么样的数据
        List<Student> data = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Student student = new Student();
            student.setStuNo(i);
            student.setStuName("强总"+i);
            data.add(student);
        }
        //c.在哪个sheet上写数据
        EasyExcel.write(filePath,Student.class).sheet("学生列表").doWrite(data);
    }

    //2.从excel中读数据
    @Test
    public void testReadExcel(){
        //a.往哪个文件当中写
        String filePath="C:\\200621\\work\\one.xlsx";
        //b.读到数据之后如何操作
        EasyExcel.read(filePath,Student.class,new ReadStudentListener()).doReadAll();
    }
}
