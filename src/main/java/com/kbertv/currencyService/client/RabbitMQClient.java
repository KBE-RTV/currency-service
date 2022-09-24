package com.kbertv.currencyService.client;

import com.kbertv.currencyService.model.dto.CelestialBodiesMessageDTO;
import com.kbertv.currencyService.model.dto.PlanetarySystemsMessageDTO;
import com.kbertv.currencyService.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class RabbitMQClient {

    @Autowired
    CurrencyService currencyService;

    @RabbitListener(queues = {"${rabbitmq.currencyService.queue.call}"})
    public String receiveConversionAndSendConvertedAmount(String callAsJson) {
        log.info("RECEIVED: " + callAsJson);

        String responseMessageAsJson;

        try {
            CelestialBodiesMessageDTO message = currencyService.parseMessageToCelestialBodiesDTO(callAsJson);

            CelestialBodiesMessageDTO responseMessageDTO = currencyService.convertCurrencyForCelestialBodies(message);

            responseMessageAsJson = currencyService.parseMessageCelestialBodiesDTOToJson(responseMessageDTO);

        } catch (Exception e) {
            PlanetarySystemsMessageDTO message = currencyService.parseMessageToPlanetarySystemsDTO(callAsJson);

            PlanetarySystemsMessageDTO responseMessageDTO = currencyService.convertCurrencyForPlanetarySystems(message);

            responseMessageAsJson = currencyService.parseMessagePlanetarySystemsDTOToJson(responseMessageDTO);

        }
        return responseMessageAsJson;
    }

}
