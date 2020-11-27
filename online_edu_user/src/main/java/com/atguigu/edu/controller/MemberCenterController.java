package com.atguigu.edu.controller;


import com.atguigu.edu.service.MemberCenterService;
import com.atguigu.response.MemberCenterVO;
import com.atguigu.response.RetVal;
import io.jsonwebtoken.Claims;
import com.atguigu.edu.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author huluo
 * @since 2020-11-12
 */
@RestController
@RequestMapping("/member/center")
@CrossOrigin
public class MemberCenterController {
    @Autowired
    private MemberCenterService memberCenterService;

    //统计每天有多少人注册
    @GetMapping("queryRegisterNum/{day}")
    public RetVal queryRegisterNum(@PathVariable String day){
        Integer registerNum = memberCenterService.queryRegisterNum(day);
        return RetVal.success().data("registerNum",registerNum);
    }

    //通过token获取用户信息
    @GetMapping("getUserInfoByToken/{token}")
    public RetVal getUserInfoByToken(@PathVariable("token") String token){
        Claims claims = JwtUtils.checkJWT(token);
        String id =(String) claims.get("id");
        String nickname =(String) claims.get("nickname");
        String avatar =(String) claims.get("avatar");
        MemberCenterVO memberCenterVO = new MemberCenterVO();
        memberCenterVO.setId(id);
        memberCenterVO.setAvatar(avatar);
        memberCenterVO.setNickname(nickname);
        return RetVal.success().data("memberCenterVO",memberCenterVO);
    }
}

