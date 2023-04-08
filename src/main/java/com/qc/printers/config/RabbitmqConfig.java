package com.qc.printers.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitmqConfig
 * @Description TODO
 * @Author 胡泽
 * @Date 2019/12/17 12:35
 * @Version 1.0
 */
@Configuration
public class RabbitmqConfig {

    public static final String QUEUE_INFORM_WORD = "queue_inform_word";

    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    public static final String ROUTINGKEY_WORD="inform.#.word.#";


    //声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    //声明QUEUE_INFORM_WORD队列
    @Bean(QUEUE_INFORM_WORD)
    public Queue QUEUE_INFORM_WORD(){
        return new Queue(QUEUE_INFORM_WORD);
    }


    //ROUTINGKEY_WORD队列绑定交换机，指定routingKey
    @Bean
    public Binding BINDING_QUEUE_INFORM_WORD(@Qualifier(QUEUE_INFORM_WORD) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_WORD).noargs();
    }


}