package com.atguigu.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//继承RuntimeException  遇到异常之后事务可以回滚
//继承Excetion          不能回滚
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("这是一个自定义异常")
public class EduException  extends RuntimeException{
    @ApiModelProperty(value = "异常编码")
    private Integer code;
    @ApiModelProperty(value = "异常信息")
    private String message;
}
