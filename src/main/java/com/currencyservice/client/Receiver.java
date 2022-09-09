package com.currencyservice.client;

import com.currencyservice.config.CurrencyApplicationConfig;
import com.currencyservice.model.DTO.MessageDTO;
import com.currencyservice.model.PlanetarySystem;
import com.currencyservice.service.CurrencyConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class Receiver {

    @RabbitListener(queues = CurrencyApplicationConfig.CURRENCY_SERVICE_CALL_QUEUE_NAME)
    public void receiveConversionAndSendConvertedAmount(String callAsJson) {
        MessageDTO message = CurrencyConverter.parseMessageToDTO(callAsJson);

        MessageDTO responseMessage = CurrencyConverter.convertCurrencyForMessage(message);

        String responseMessageAsJson = CurrencyConverter.parseMessageDTOToJson(responseMessage);

        Sender.sendConvertedAmount(responseMessageAsJson);

    }

}
