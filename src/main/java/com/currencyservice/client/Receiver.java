package com.currencyservice.client;

import com.currencyservice.config.CurrencyApplicationConfig;
import com.currencyservice.service.CurrencyConverter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class Receiver {

    @RabbitListener(queues = CurrencyApplicationConfig.CURRENCY_SERVICE_CALL_QUEUE_NAME)
    public void receiveConversionAndSendConvertedAmount(String conversionJson) {
        String convertedAmountsAsJson = CurrencyConverter.convertAmounts(conversionJson);

        System.out.println("Sent: " + convertedAmountsAsJson);

        Sender.sendConvertedAmount(convertedAmountsAsJson);
    }

}
