package com.ruoyi.mq.bean;

/**
 * @author japhet_jiu
 * @version 1.0
 */
public class SysConstant {

    /**
     * 测试队列
     */
    public static final String QUEUE_TEST_HELLO = "QUEUE_TEST_HELLO";

    /**
     * 一个队列多个消费者
     */
    public static final String JAPHET_FANOUTEXCHANGE = "JAPHET_FANOUTEXCHANGE";
    public static final String JAPHET_QUEUE1 = "JAPHET_QUEUE1";
    public static final String JAPHET_QUEUE2 = "JAPHET_QUEUE2";


    public static final String JAPHET_DIRECTEXCHANGE = "JAPHET_DIRECTEXCHANGE";
    public static final String JAPHET_DIRECTQUEUE1 = "JAPHET_DIRECTQUEUE1";
    public static final String JAPHET_DIRECTQUEUE2 = "JAPHET_DIRECTQUEUE2";
    public static final String JAPHET_KEY1 = "JAPHET_KEY1";
    public static final String JAPHET_KEY2 = "JAPHET_KEY2";
    public static final String JAPHET_KEY3 = "JAPHET_KEY3";


    public static final String JAPHET_OBJECTQUEUE = "JAPHET_OBJECTQUEUE";


    /**死信交换机**/
    public static final String SYS_ORDER_DELAY_EXCHANGE = "SYS_ORDER_DELAY_EXCHANGE";
    /**接收死信队列消息**/
    public static final String SYS_ORDER_RECEIVE_EXCHANGE = "SYS_ORDER_RECEIVE_EXCHANGE";
    /**死信接收队列**/
    public static final String SYS_ORDER_RECEIVE_QUEUE = "SYS_ORDER_RECEIVE_QUEUE";
    /**延时队列**/
    public static final String SYS_ORDER_DELAY_QUEUE = "SYS_ORDER_DELAY_QUEUE";
    /**路由key**/
    public static final String SYS_ORDER_RECEIVE_KEY = "SYS_ORDER_RECEIVE_KEY";
    /**死信队列路由key**/
    public static final String SYS_ORDER_DELAY_KEY = "SYS_ORDER_DELAY_KEY";
}
