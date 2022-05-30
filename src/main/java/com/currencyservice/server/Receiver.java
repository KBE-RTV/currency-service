package com.currencyservice.server;

import com.currencyservice.config.CurrencyApplicationConfig;
import com.currencyservice.service.CurrencyConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class Receiver {

    ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = CurrencyApplicationConfig.queueName)
    public double receiveConversionAndSendAmount(String conversionJson) throws IOException {
        ObjectNode conversionNode = new ObjectMapper().readValue(conversionJson, ObjectNode.class);

        double amount = conversionNode.get("amount").doubleValue();
        String currencyToConvertFrom = conversionNode.get("currencyToConvertFrom").asText();
        String currencyToConvertTo = conversionNode.get("currencyToConvertTo").asText();

        System.out.println("Received: \n"
                + "amount: " + amount + "\n"
                + "currencyToConvertFrom: " + currencyToConvertFrom + "\n"
                + "currencyToConvertTo: " + currencyToConvertTo + "\n");

        double convertedAmount = CurrencyConverter.convertCurrency(amount, currencyToConvertFrom, currencyToConvertTo);

        System.out.println("Sent: " + convertedAmount);

        return convertedAmount;
    }

}
