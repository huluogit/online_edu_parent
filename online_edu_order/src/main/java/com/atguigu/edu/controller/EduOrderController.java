package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduOrder;
import com.atguigu.edu.service.EduOrderService;
import com.atguigu.edu.utils.JwtUtils;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
public class EduOrderController {
    @Autowired
    private EduOrderService orderService;
    //1.根据课程id进行下单
    @GetMapping("createOrder/{courseId}")
    public RetVal createOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = orderService.createOrder(courseId, memberId);
        return RetVal.success().data("orderNo",orderNo);
    }

    //2.根据订单id查询订单信息
    @GetMapping("getOrderByOrderNo/{orderNo}")
    public RetVal getOrderByOrderNo(@PathVariable String orderNo){
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        EduOrder orderInfo = orderService.getOne(wrapper);
        return RetVal.success().data("orderInfo",orderInfo);
    }

    //3.在微信端下订单
    @GetMapping("createPayQrCode/{orderNo}")
    public RetVal createPayQrCode(@PathVariable String orderNo){
        Map<String, Object> payQrCode = orderService.createPayQrCode(orderNo);
        return RetVal.success().data(payQrCode);
    }

    //4.查询订单支付状态
    @GetMapping("queryPayState/{orderNo}")
    public RetVal queryPayState(@PathVariable String orderNo){
        Map<String, String> txRetMap = orderService.queryPayState(orderNo);
        if(txRetMap.get("trade_state").equals("SUCCESS")){
            //其他逻辑 实际工作中需要做的操作还有很多
            orderService.updateOrderState(txRetMap);
            return RetVal.success().message("支付成功");
        }else{
            return RetVal.error().message("支付失败");
        }
    }
}

