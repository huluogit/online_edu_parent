package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-10-16
 */
public interface EduOrderService extends IService<EduOrder> {

    String createOrder(String courseId, String memberId);

    Map<String, Object> createPayQrCode(String orderNo);

    Map<String, String> queryPayState(String orderNo);

    void updateOrderState(Map<String, String> txRetMap);
}
