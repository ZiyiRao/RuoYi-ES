package com.ruoyi.mq.config;

import com.rabbitmq.client.AMQP;
import com.ruoyi.mq.bean.SysConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 * @author japhet_jiu
 * @version 1.0
 */

@Configuration
public class RabbitMQConfig {

    /**
     * 测试发送消息到MQ
     * @return
     */
    @Bean
    public Queue testHello() {
        return new Queue(SysConstant.QUEUE_TEST_HELLO);
    }


    /**
     *创建一个交换机 fanoutExchange
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(SysConstant.JAPHET_FANOUTEXCHANGE);
    }

    /**
     * 创建一个队列 queue1
     * @return
     */
    @Bean
    public Queue queue1(){
        return new Queue(SysConstant.JAPHET_QUEUE1);
    }

    /**
     * 吧 queue1 绑定到 fanoutExchange 里面去
     * @param fanoutExchange
     * @param queue1
     * @return
     */
    @Bean
    public Binding binding(FanoutExchange fanoutExchange,Queue queue1){
        return BindingBuilder.bind(queue1).to(fanoutExchange);
    }
    /**
     * 创建一个队列 queue2
     * @return
     */
    @Bean
    public Queue queue2(){
        return new Queue(SysConstant.JAPHET_QUEUE2);
    }
    /**
     * 吧 queue2 绑定到 fanoutExchange 里面去
     * @param fanoutExchange
     * @param queue2
     * @return
     */
    @Bean
    public Binding binding2(FanoutExchange fanoutExchange,Queue queue2){
        return BindingBuilder.bind(queue2).to(fanoutExchange);
    }

    /**
     * 创建一个队列 object
     * @return
     */
    @Bean
    public Queue objectQueue(){
        return new Queue(SysConstant.JAPHET_OBJECTQUEUE);
    }


    /**
     * 死信交换机
     * @return
     */
    @Bean
    public DirectExchange sysOrderDelayExchange() {
        return new DirectExchange(SysConstant.SYS_ORDER_DELAY_EXCHANGE);
    }
    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue sysOrderDelayQueue() {
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("x-dead-letter-exchange",SysConstant.SYS_ORDER_RECEIVE_EXCHANGE); //指定死信送往的交换机
        map.put("x-dead-letter-routing-key", SysConstant.SYS_ORDER_RECEIVE_KEY); //指定死信的routingkey
        return new Queue(SysConstant.SYS_ORDER_DELAY_QUEUE, true, false, false, map);
    }

    /**
     * 给死信队列绑定死信交换机
     * @return
     */
    @Bean
    public Binding sysOrderDelayBinding() {
        return BindingBuilder.bind(sysOrderDelayQueue()).to(sysOrderDelayExchange()).with(SysConstant.SYS_ORDER_DELAY_KEY);
    }

    /**
     * 死信接收交换机,用于接收死信队列的消息
     * @return
     */
    @Bean
    public DirectExchange sysOrderReceiveExchange() {
        return new DirectExchange(SysConstant.SYS_ORDER_RECEIVE_EXCHANGE);
    }

    /**
     * 死信接收队列
     * @return
     */
    @Bean
    public Queue sysOrderReceiveQueue() {
        return new Queue(SysConstant.SYS_ORDER_RECEIVE_QUEUE);
    }

    /**
     * 死信接收交换机绑定接收死信队列消费队列
     * @return
     */
    @Bean
    public Binding sysOrdeReceiveBinding() {
        return BindingBuilder.bind(sysOrderReceiveQueue()).to(sysOrderReceiveExchange()).with(SysConstant.SYS_ORDER_RECEIVE_KEY);
    }
}
