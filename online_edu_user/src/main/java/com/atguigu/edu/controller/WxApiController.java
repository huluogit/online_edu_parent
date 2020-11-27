package com.atguigu.edu.controller;

import com.atguigu.edu.entity.MemberCenter;
import com.atguigu.edu.service.MemberCenterService;
import com.atguigu.edu.utils.HttpClientUtils;
import com.atguigu.edu.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

//交给spring容器管理 表示这个是控制层
@Controller
//路径地址 映射给前端
@RequestMapping("/api/ucenter/wx/")
//解决跨域问题
@CrossOrigin
public class WxApiController {
//  @Value用于从application配置文件中读取相对应的值
    //appid
    @Value("${wx.open.app_id}")
    private String WX_OPEN_APP_ID;
    //微信开放平台 appsecret
    @Value("${wx.open.app_secret}")
    private String WX_OPEN_APP_SECRET;
    //redirect_uri   用户允许授权后，将会重定向到redirect_uri的网址上，并且带上code和state参数
    @Value("${wx.open.redirect_url}")
    private String WX_OPEN_REDIRECT_URL;

    //自动注入
    @Autowired
    private MemberCenterService memberCenterService;

    //1.获取一个二维码 @GetMapping表示获取或者查询请求
        @GetMapping("login")
        public String qrCode() throws Exception {
        //微信官网提供
       /* https://open.weixin.qq.com/connect/qrconnect?    固定的开头
        appid=APPID & redirect_uri=REDIRECT_URI
         & response_type=code & scope=SCOPE
         & state=STATE # wechat_redirect
            参数	        是否必须	               说明
            appid	          是	            应用唯一标识
            redirect_uri	  是	        请使用urlEncode对链接进行处理
            response_type	  是	             填code
            scope	          是	    应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login
            state	          否	用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验  */
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String state = "aguigu";//该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        String encodeURL = URLEncoder.encode(WX_OPEN_REDIRECT_URL, "UTF-8"); //把地址url转换为字符编码为Utf-8的格式
        baseUrl = String.format(baseUrl,WX_OPEN_APP_ID,encodeURL,state);//使用gson进行格式化，转换为json
        return "redirect:" + baseUrl;//重定向到新的页面
    }

    //2.编写callback回调方法
    @GetMapping("callback")
    public String callback(String code) throws Exception {
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        baseAccessTokenUrl = String.format(baseAccessTokenUrl,WX_OPEN_APP_ID,WX_OPEN_APP_SECRET,code);
        //利用一个httpclient发起一个http请求
        String retVal = HttpClientUtils.get(baseAccessTokenUrl);

        Gson gson = new Gson();
        HashMap retMap = gson.fromJson(retVal, HashMap.class);
        String access_token = (String) retMap.get("access_token");
        String openid = (String) retMap.get("openid");
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        userInfoUrl = String.format(userInfoUrl, access_token, openid);
        String userInfo = HttpClientUtils.get(userInfoUrl);
        System.out.println(userInfo);
        HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
        String nickname = (String) userInfoMap.get("nickname");
        String headimgurl = (String) userInfoMap.get("headimgurl");

        //拿到用户信息之后一个存储在我们的数据库中
        QueryWrapper<MemberCenter> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        MemberCenter exitMemberCenter = memberCenterService.getOne(wrapper);
        if(exitMemberCenter==null) {
            exitMemberCenter = new MemberCenter();
            exitMemberCenter.setAvatar(headimgurl);
            exitMemberCenter.setNickname(nickname);
            exitMemberCenter.setOpenid(openid);
            memberCenterService.save(exitMemberCenter);
        }
        //把用户信息加密成一个token信息
        String token = JwtUtils.geneJsonWebToken(exitMemberCenter);
        return "redirect:http://127.0.0.1:3000?token="+token;
    }
}
