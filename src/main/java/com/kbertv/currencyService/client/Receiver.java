package com.kbertv.currencyService.client;

import com.kbertv.currencyService.model.DTO.MessageDTO;
import com.kbertv.currencyService.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class Receiver {

    @Autowired
    Sender sender;

    @Autowired
    CurrencyService currencyService;

    @RabbitListener(queues = {"${rabbitmq.currencyService.queue.call}"})
    public String receiveConversionAndSendConvertedAmount(String callAsJson) {
        log.info("RECEIVED: " + callAsJson);

        MessageDTO message = currencyService.parseMessageToDTO(callAsJson);

        MessageDTO responseMessage = currencyService.convertCurrencyForMessage(message);

        String responseMessageAsJson = currencyService.parseMessageDTOToJson(responseMessage);

        //sender.sendConvertedAmount(responseMessageAsJson);
        return responseMessageAsJson;
    }

}
