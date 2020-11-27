package com.atguigu.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ChapterVO {
    @ApiModelProperty(value = "章节ID")
    private String id;
    @ApiModelProperty(value = "章节名称")
    private String title;
    @ApiModelProperty(value = "章节名称")
    private List<SectionVO> children=new ArrayList<>();
}
