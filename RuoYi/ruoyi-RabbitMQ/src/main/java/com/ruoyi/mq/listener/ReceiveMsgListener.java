package com.ruoyi.mq.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.mq.bean.LoginLog;
import com.ruoyi.mq.bean.SysConstant;
import com.ruoyi.mq.service.ILoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 接收消息监听类
 * @author japhet_jiu
 * @version 1.0
 */
@Component
@EnableRabbit
@Slf4j
public class ReceiveMsgListener {

    @Autowired
    private ILoginLogService loginLogService;

    /**
     * //正常的消费者
     * 监听队列的消息
     * @RabbitListener(queues = SysConstant.QUEUE_TEST_HELLO) 代表监听的队列名称
     * @param msg 消息内容
     */
    @RabbitListener(queues = SysConstant.QUEUE_TEST_HELLO)
    @RabbitHandler
    public void getTestMsg(String msg) {
        log.info("---------->接收到的消息:{}",msg);
    }


    /**
     * 接受queue1的消息
     * @param msg
     */
    @RabbitListener(queues = SysConstant.JAPHET_QUEUE1)
    @RabbitHandler
    public void listenSendToManyConsumerQueue1(String msg) {
        log.info("---------->接收到queue1的消息:{}",msg);
    }
    @RabbitListener(queues = SysConstant.JAPHET_QUEUE2)
    @RabbitHandler
    public void listenSendToManyConsumerQueue2(String msg) {
        log.info("---------->接收到queue2的消息:{}",msg);
    }

    /**
     * 定义direct exchange,绑定队列，设置路由key，
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name = SysConstant.JAPHET_DIRECTQUEUE1),
            exchange=@Exchange(name = SysConstant.JAPHET_DIRECTEXCHANGE,type = ExchangeTypes.DIRECT),
            key = {SysConstant.JAPHET_KEY1,SysConstant.JAPHET_KEY3}
    ))
    @RabbitHandler
    public void queueRouteKey(String msg){
        log.info("---------->接收到JAPHET_DIRECTQUEUE1的消息:{}",msg);
    }
    /**
     * 定义direct exchange,绑定队列，设置路由key，
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name = SysConstant.JAPHET_DIRECTQUEUE2),
            exchange=@Exchange(name = SysConstant.JAPHET_DIRECTEXCHANGE,type = ExchangeTypes.DIRECT),
            key = {SysConstant.JAPHET_KEY2,SysConstant.JAPHET_KEY3}
    ))
    @RabbitHandler
    public void queueRouteKey2(String msg){
        log.info("---------->接收到JAPHET_DIRECTQUEUE2的消息:{}",msg);
    }

    /**
     * 获取object类型的值
     */
    @RabbitListener(queues = SysConstant.JAPHET_OBJECTQUEUE)
    @RabbitHandler
    public void listenerObjectConsumer(Map<String,Object> msg){
        log.info("---------->接收到JAPHET_DIRECTQUEUE2的消息:{}",msg);
    }

    /**
     * 获取到的延时消息
     * 这里接收到消息进行对应的业务处理(例如:登录该程序，进行一个短信通知)
     * @param msg
     */
    @RabbitListener(queues = SysConstant.SYS_ORDER_RECEIVE_QUEUE)
    @RabbitHandler
    public void getdelayMsg(String msg) {
        log.info("MQ接收消息时间:{},消息内容:{}", DateUtil.formatDateTime(DateUtil.date()),msg);
        log.info("------->这里实现登录通知...");
        JSONObject jsonObject = JSONObject.parseObject(msg);
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginOperation(jsonObject.getString("loginOperation"));
        loginLog.setLoginTime(jsonObject.getDate("loginTime"));
        loginLog.setLoginIp(jsonObject.getString("loginIp"));
        loginLogService.insertLoginLog(loginLog);
    }
}
