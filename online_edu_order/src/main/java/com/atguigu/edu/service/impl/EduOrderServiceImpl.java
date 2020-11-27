package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduOrder;
import com.atguigu.edu.entity.EduPayLog;
import com.atguigu.edu.mapper.EduOrderMapper;
import com.atguigu.edu.service.EduOrderService;
import com.atguigu.edu.service.EduPayLogService;
import com.atguigu.edu.utils.HttpClient;
import com.atguigu.edu.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-10-16
 */
@Service
public class EduOrderServiceImpl extends ServiceImpl<EduOrderMapper, EduOrder> implements EduOrderService {
    @Value("${wx.pay.app_id}")
    private String WX_PAY_APP_ID;
    @Value("${wx.pay.mch_id}")
    private String WX_PAY_MCH_ID;
    @Value("${wx.pay.spbill_create_ip}")
    private String WX_PAY_SPBILL_IP;
    @Value("${wx.pay.notify_url}")
    private String WX_PAY_NOTIFY_URL;
    @Value("${wx.pay.xml_key}")
    private String WX_PAY_XML_KEY;
    @Autowired
    private EduPayLogService payLogService;

    @Override
    public String createOrder(String courseId, String memberId) {
        EduOrder order = new EduOrder();
        //a.接受两个参数memberid和courseId
        //b.保证订单不重复
        //生成订单号 订单号唯一
        String orderNo = OrderNoUtil.getOrderNo();
        //设置订单号
        order.setOrderNo(orderNo);
        //设置课程id 由参数传进去
        order.setCourseId(courseId);
        //c.课程信息需要通过RPC的形式调用其他微服务
        //课程名称
        order.setCourseTitle("微信支付");
        //课程封面
        order.setCourseCover("http://img20.360buyimg.com/imgzone/jfs/t1/124992/4/4948/41240/5ee83ef3E37b31378/c2c0d004c3fbabd0.jpg");
        //d.用户信息需要通过RPC的形式调用其他微服务
        //讲师的名称设置
        order.setTeacherName("张老师");
        //会员的id
        order.setMemberId(memberId);
        //会员的名字
        order.setNickName("胡箩");
        //会员的手机
        order.setMobile("1315678234");
        //订单的金额
        order.setTotalFee(new BigDecimal(0.01));
        //支付类型（1：微信 2：支付宝)
        order.setPayType(1);
        //订单状态（0：未支付 1：已支付）
        order.setStatus(0);
        //e.保存到数据库中
        baseMapper.insert(order);
        //f.返回订单号信息给前端进行展示
        return orderNo;
    }

    @Override
    public Map<String, Object> createPayQrCode(String orderNo) {
        //new 一个wrapper进行条件封装
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        //通过order_no进行查询
        wrapper.eq("order_no",orderNo);
        //通过order_no查询订单的全部信息
        EduOrder orderInfo = baseMapper.selectOne(wrapper);

        //a.把需要的参数先封装成map----->xml
        Map<String, String> requestParams = new HashMap<>();
        //公众账号ID
        requestParams.put("appid", WX_PAY_APP_ID);
        //商户号
        requestParams.put("mch_id", WX_PAY_MCH_ID);
        //随机字符串
        requestParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        requestParams.put("body", orderInfo.getCourseTitle());
        //商户订单号
        requestParams.put("out_trade_no", orderNo);
        //标价金额
        String totalFee=orderInfo.getTotalFee().multiply(new BigDecimal(100)).intValue()+"";
        requestParams.put("total_fee", totalFee);
        //终端IP
        requestParams.put("spbill_create_ip", WX_PAY_SPBILL_IP);
        //通知地址
        requestParams.put("notify_url", WX_PAY_NOTIFY_URL);
        //交易类型
        requestParams.put("trade_type", "NATIVE");
        //b.调用提供的统一下单接口
        try {
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String paramsXml = WXPayUtil.generateSignedXml(requestParams, WX_PAY_XML_KEY);
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramsXml);
            httpClient.post();


            String content = httpClient.getContent();
            Map<String, String> txRetMap = WXPayUtil.xmlToMap(content);
            //支付二维码连接
            String qrCodeUrl = txRetMap.get("code_url");
            Map<String, Object> retMap = new HashMap<>();
            retMap.put("qrCodeUrl",qrCodeUrl);
            retMap.put("orderNo",orderNo);
            retMap.put("totalFee",orderInfo.getTotalFee());
            retMap.put("courseId",orderInfo.getCourseId());
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //c.得到接口返回的xml数据信息
        return null;
    }

    @Override
    public Map<String, String> queryPayState(String orderNo) {
        //a.把需要的参数先封装成map----->xml
        Map<String, String> requestParams = new HashMap<>();
        //公众账号ID
        requestParams.put("appid", WX_PAY_APP_ID);
        //商户号
        requestParams.put("mch_id", WX_PAY_MCH_ID);
        //随机字符串
        requestParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //商户订单号
        requestParams.put("out_trade_no", orderNo);

        try {
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            String paramsXml = WXPayUtil.generateSignedXml(requestParams, WX_PAY_XML_KEY);
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramsXml);
            httpClient.post();

            String content = httpClient.getContent();
            Map<String, String> txRetMap = WXPayUtil.xmlToMap(content);
            return txRetMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOrderState(Map<String, String> txRetMap) {
        //a.更改数据库中订单状态
        String orderNo = txRetMap.get("out_trade_no");
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        EduOrder orderInfo = baseMapper.selectOne(wrapper);
        orderInfo.setStatus(1);
        baseMapper.updateById(orderInfo);
        //b.把支付记录写到日志表里面
        QueryWrapper<EduPayLog> logWrapper = new QueryWrapper<>();
        logWrapper.eq("order_no",orderNo);
        EduPayLog existPaylog = payLogService.getOne(logWrapper);
        if(existPaylog==null){
            EduPayLog payLog = new EduPayLog();
            payLog.setOrderNo(orderNo);
            payLog.setPayTime(new Date());
            payLog.setTotalFee(orderInfo.getTotalFee());
            payLog.setTransactionId(txRetMap.get("transaction_id"));
            payLog.setTradeState(txRetMap.get("trade_state"));
            payLog.setPayType(1);
            payLogService.save(payLog);
        }
    }
}
