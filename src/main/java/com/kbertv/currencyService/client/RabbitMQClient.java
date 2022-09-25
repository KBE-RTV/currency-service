package com.kbertv.currencyService.client;

import com.kbertv.currencyService.model.dto.CelestialBodiesMessageDTO;
import com.kbertv.currencyService.model.dto.PlanetarySystemsMessageDTO;
import com.kbertv.currencyService.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * class for receiving messages on rabbitMQ queues and replying to them
 */
@Component
@Slf4j
class RabbitMQClient {

    @Autowired
    CurrencyService currencyService;

    @RabbitListener(queues = {"${rabbitmq.currencyService.queue.call}"})
    public String receiveConversionAndSendConvertedAmount(String messageAsJson) {
        log.info("RECEIVED: " + messageAsJson);

        String responseMessageAsJson;

        try {
            CelestialBodiesMessageDTO celestialBodiesMessageDTO = currencyService.parseMessageToCelestialBodiesDTO(messageAsJson);

            CelestialBodiesMessageDTO responseMessageDTO = currencyService.convertCurrencyForCelestialBodies(celestialBodiesMessageDTO);

            responseMessageAsJson = currencyService.parseMessageCelestialBodiesDTOToJson(responseMessageDTO);

        } catch (Exception e) {
            PlanetarySystemsMessageDTO planetarySystemsMessageDTO = currencyService.parseMessageToPlanetarySystemsDTO(messageAsJson);

            PlanetarySystemsMessageDTO responseMessageDTO = currencyService.convertCurrencyForPlanetarySystems(planetarySystemsMessageDTO);

            responseMessageAsJson = currencyService.parseMessagePlanetarySystemsDTOToJson(responseMessageDTO);

        }
        return responseMessageAsJson;
    }

}
