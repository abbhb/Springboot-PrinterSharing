package com.qc.printers;

import com.qc.printers.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMQTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void test01(){

        String url = "http://10.15.245.153:9090/aistudio/9ac1a88bee9647eabb96d9ff588d3c05.docx";

        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.word", url);
    }
}
