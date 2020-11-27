package com.atguigu.edu.service;

import com.atguigu.response.RetVal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "EDU-USER")
public interface UserServiceFeign {
    @GetMapping("/member/center/queryRegisterNum/{day}")
    public RetVal queryRegisterNum(@PathVariable("day") String day);
}
