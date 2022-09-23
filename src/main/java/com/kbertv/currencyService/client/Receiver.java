package com.kbertv.currencyService.client;

import com.kbertv.currencyService.model.DTO.CelestialBodiesMessageDTO;
import com.kbertv.currencyService.model.DTO.PlanetarySystemsMessageDTO;
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

        String responseMessageAsJson;

        try {
            CelestialBodiesMessageDTO message = currencyService.parseMessageToCelestialBodiesDTO(callAsJson);

            CelestialBodiesMessageDTO responseMessage = currencyService.convertCurrencyForCelestialBodies(message);

            responseMessageAsJson = currencyService.parseMessageCelestialBodiesDTOToJson(responseMessage);

        } catch (Exception e) {
            PlanetarySystemsMessageDTO message = currencyService.parseMessageToPlanetarySystemsDTO(callAsJson);

            PlanetarySystemsMessageDTO responseMessage = currencyService.convertCurrencyForPlanetarySystems(message);

            responseMessageAsJson = currencyService.parseMessagePlanetarySystemsDTOToJson(responseMessage);

        }
        return responseMessageAsJson;
    }

}
