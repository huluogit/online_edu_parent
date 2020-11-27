package com.atguigu.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {
    @ExcelProperty(value = "学生编号",index = 1)
    private Integer stuNo;
    @ExcelProperty(value = "学生姓名",index = 0)
    private String stuName;

}
