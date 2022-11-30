package com.ruoyi.mq.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.mq.bean.LoginLog;
import com.ruoyi.mq.bean.SysConstant;
import com.ruoyi.mq.service.serviceImpl.MsgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author japhet_jiu
 * @version 1.0
 */

@RestController
@RequestMapping("rabbitmq")
@Slf4j
public class MQController {

    @Autowired
    private MsgServiceImpl msgService;


    @GetMapping("sendMsg")
    public String sendMsg() {
        log.info("发送消息时间:" + DateUtil.formatDateTime(DateUtil.date()));
        //正常的生产者
        msgService.sendMsg(SysConstant.QUEUE_TEST_HELLO,"hello japhet_jiu");
        return JSONUtil.toJsonStr("发送消息成功"+new Date());
    }

    /**
     * 一个消息很多个消费者使用
     * 一个队列绑定多个消费者
     * 需要通过交换机
     * fanoutExchange
     */

    @GetMapping("sendToManyConsumer")
    public String sendManyConsumer() {
        //交换机名称
        String exchangeName = SysConstant.JAPHET_FANOUTEXCHANGE;
        //发送内容
        String msg = "给JAPHET_EXCHANGE绑定每个交换机队列发送消息";
        //发送消息
        msgService.sendManyConsumer(exchangeName,msg);
        return JSONUtil.toJsonStr("发送消息成功"+new Date());
    }

    /**
     *
     * directExchange 会吧收到的消息根据规则路由到指定的queue 【路由模式】
     *
     */
    @GetMapping("sendToManyConsumerByRoute")
    public String sendToManyConsumerByRoute() {
        //交换机名称
        String exchangeName = SysConstant.JAPHET_DIRECTEXCHANGE;
        //发送内容
        String msg = "给路由key3的发消息";
        String routeKey = SysConstant.JAPHET_KEY3;
        //发送消息
        msgService.sendToManyConsumerByRoute(exchangeName,routeKey,msg);
        return JSONUtil.toJsonStr("发送消息成功"+new Date());
    }

    /**
     *发送参数类型是实体类、object、等其他类型
     *
     * 引入依赖  jackson-databind
     *
     */
    @GetMapping("sendObjectConsumer")
    public String sendObjectConsumer() {
        Map<String,Object> mapMsg = new HashMap<String,Object>();
        mapMsg.put("name","japhet_jiu");
        mapMsg.put("csdn","https://blog.csdn.net/japhet_jiu");
        mapMsg.put("gitee","https://gitee.com/J-LJJ");
        //发送内容
        String queue = SysConstant.JAPHET_OBJECTQUEUE;
        //发送消息
        msgService.sendObjectConsumer(queue,mapMsg);
        return JSONUtil.toJsonStr("发送消息成功"+new Date());
    }

    @GetMapping("sendLaterMsg")
    public String sendLaterMsg() {
        log.info("发送延时消息时间:" + DateUtil.formatDateTime(DateUtil.date()));

        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp("1.204.219.129");
        loginLog.setLoginOperation("测试mq");
        loginLog.setLoginTime(new Date());
        loginLog.setLoginUserId(1L);

        msgService.sendDelayMsgToMQ(SysConstant.SYS_ORDER_DELAY_EXCHANGE,SysConstant.SYS_ORDER_DELAY_KEY, JSONUtil.toJsonStr(loginLog),10*3000);//30秒钟后执行
        return JSONUtil.toJsonStr("发送延时消息成功"+new Date());
    }
}
