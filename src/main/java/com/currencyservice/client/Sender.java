package com.currencyservice.client;

import com.currencyservice.config.CurrencyApplicationConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    public static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        Sender.rabbitTemplate = rabbitTemplate;
    }

    public static void sendConvertedAmount(String convertedAmountsAsJson) {

        rabbitTemplate.convertAndSend(CurrencyApplicationConfig.exchange.getName(), CurrencyApplicationConfig.CURRENCY_SERVICE_RESPONSE_ROUTING_KEY, convertedAmountsAsJson);

        System.out.println("SENT: " + convertedAmountsAsJson + "\n");
    }
}
