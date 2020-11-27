package com.atguigu.handler;

import com.atguigu.exception.EduException;
import com.atguigu.response.RetVal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//只要出现异常 就会交给这个类进行处理 全局异常捕获
/*
*@ControllerAdvice是在类上声明的注解，其用法主要有三点：

@ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的；
@InitBinder注解标注的方法：用于请求中注册自定义参数的解析，从而达到自定义请求参数格式的目的；
@ModelAttribute注解标注的方法：表示此方法会在执行目标Controller方法之前执行 。
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RetVal error(Exception e){
        e.printStackTrace();
        System.out.println("全局异常生效了");
        return RetVal.error().message("全局异常生效了");
    }
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public RetVal error(ArithmeticException e){
        e.printStackTrace();
        System.out.println("特殊异常生效了");
        return RetVal.error().message("特殊异常生效了");
    }
    @ExceptionHandler(EduException.class)
    @ResponseBody
    public RetVal error(EduException e){
        e.printStackTrace();
        System.out.println(e.getMessage());
        return RetVal.error().message(e.getMessage());
    }
}
