package com.kbertv.currencyService.client;

import com.kbertv.currencyService.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sender {

    @Value("${rabbitmq.currencyService.key.response}")
    private String currencyServiceResponseRoutingKey;

    public static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        Sender.rabbitTemplate = rabbitTemplate;
    }

    public void sendConvertedAmount(String convertedAmountsAsJson) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.exchange.getName(), currencyServiceResponseRoutingKey, convertedAmountsAsJson);

        log.info("SENT: " + convertedAmountsAsJson);
    }
}
