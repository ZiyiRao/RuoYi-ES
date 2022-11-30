package com.ruoyi.mq.service.serviceImpl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author japhet_jiu
 * @version 1.0
 */
@Service
public class MsgServiceImpl {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送普通消息到消费队列
     * @param queue
     * @param msg
     */
    public void sendMsg(String queue,String msg) {
        rabbitTemplate.convertAndSend(queue,msg);
    }

    /**
     * 一个消息很多个消费者使用
     * 一个队列绑定多个消费者
     * @param exchangeName
     * @param msg
     */
    public void sendManyConsumer(String exchangeName, String msg) {
        rabbitTemplate.convertAndSend(exchangeName,"",msg);
    }

    /**
     * 路由key
     * @param exchangeName
     * @param routeKey
     * @param msg
     */
    public void sendToManyConsumerByRoute(String exchangeName, String routeKey, String msg) {
        rabbitTemplate.convertAndSend(exchangeName,routeKey,msg);
    }

    /**
     * 发送参数类型是object
     * @param queue
     * @param mapMsg
     */
    public void sendObjectConsumer(String queue, Map<String, Object> mapMsg) {
        rabbitTemplate.convertAndSend(queue,mapMsg);
    }

    /**
     * 发送延时消息到mq
     * @param exchange 死信交换机
     * @param routeKey 路由key
     * @param data 发送数据
     * @param delayTime 过期时间，单位毫秒
     */
    public void sendDelayMsgToMQ(String exchange, String routeKey, String data,int delayTime) {
        rabbitTemplate.convertAndSend(exchange, routeKey, data, message -> {
            message.getMessageProperties().setExpiration(delayTime + "");
            return message;
        });
    }

}
