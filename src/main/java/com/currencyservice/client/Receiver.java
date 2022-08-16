package com.currencyservice.client;

import com.currencyservice.config.CurrencyApplicationConfig;
import com.currencyservice.service.CurrencyConverter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class Receiver {

    @RabbitListener(queues = CurrencyApplicationConfig.CURRENCY_SERVICE_CALL_QUEUE_NAME)
    public void receiveConversionAndSendConvertedAmount(String conversionAsJson) {

        String convertedAmountsAsJson = CurrencyConverter.convertAmounts(conversionAsJson);

        System.out.println("RECEIVED: " + conversionAsJson);

        Sender.sendConvertedAmount(convertedAmountsAsJson);
    }

}
