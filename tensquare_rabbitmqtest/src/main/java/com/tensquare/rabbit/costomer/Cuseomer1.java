package com.tensquare.rabbit.costomer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ="itcast" )
public class Cuseomer1{

    @RabbitHandler
    public void get(String msg){
        System.out.println("直接模式消费消息:"+msg);
    }
}
