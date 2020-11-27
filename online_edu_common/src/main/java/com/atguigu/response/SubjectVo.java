package com.atguigu.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SubjectVo {
    @ApiModelProperty(value = "课程类别ID")
    private String id;
    @ApiModelProperty(value = "类别名称")
    private String title;
    @ApiModelProperty(value = "课程分类子节点")
    private List<SubjectVo> children=new ArrayList<>();
}
