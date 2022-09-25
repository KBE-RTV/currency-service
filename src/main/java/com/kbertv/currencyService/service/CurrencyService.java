package com.kbertv.currencyService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kbertv.currencyService.model.CelestialBody;
import com.kbertv.currencyService.model.PlanetarySystem;
import com.kbertv.currencyService.model.dto.CelestialBodiesMessageDTO;
import com.kbertv.currencyService.model.dto.PlanetarySystemsMessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * class for converting currencies and parsing json
 */
@Service
public class CurrencyService {
    @Value("${currencyapi.url}")
    private String currencyApiUrl;
    @Value("${currencyapi.key}")
    private String currencyApiKey;

    static ObjectMapper objectMapper;

    /**
     * converts the prices for a list of planetary systems contained in a messageDTO to another currency
     * @param planetarySystemsMessageDTO
     * @return received messageDTO with converted prices
     */
    public PlanetarySystemsMessageDTO convertCurrencyForPlanetarySystems(PlanetarySystemsMessageDTO planetarySystemsMessageDTO)
    {
        ArrayList<PlanetarySystem> planetarySystems = planetarySystemsMessageDTO.getPlanetarySystems();
        String currencyToConvertFrom = planetarySystemsMessageDTO.getCurrencyToConvertFrom();
        String currencyToConvertTo = planetarySystemsMessageDTO.getCurrencyToConvertTo();

        double conversionRate = getConversionRateAsDouble(currencyToConvertFrom, currencyToConvertTo);

        for (PlanetarySystem planetarySystem : planetarySystems
        ) {
            float convertedPrice = (float) (planetarySystem.getPrice() * conversionRate);
            planetarySystem.setPrice(convertedPrice);
        }

        planetarySystemsMessageDTO.setPlanetarySystems(planetarySystems);

        return planetarySystemsMessageDTO;
    }

    /**
     * converts the prices for a list of celestial bodies contained in a messageDTO to another currency
     * @param celestialBodiesMessageDTO
     * @return received messageDTO with converted prices
     */
    public CelestialBodiesMessageDTO convertCurrencyForCelestialBodies(CelestialBodiesMessageDTO celestialBodiesMessageDTO)
    {
        ArrayList<CelestialBody> celestialBodies = celestialBodiesMessageDTO.getCelestialBody();
        String currencyToConvertFrom = celestialBodiesMessageDTO.getCurrencyToConvertFrom();
        String currencyToConvertTo = celestialBodiesMessageDTO.getCurrencyToConvertTo();

        double conversionRate = getConversionRateAsDouble(currencyToConvertFrom, currencyToConvertTo);

        for (CelestialBody celestialBody : celestialBodies
        ) {
            float convertedPrice = (float) (celestialBody.getPrice() * conversionRate);
            celestialBody.setPrice(convertedPrice);
        }

        celestialBodiesMessageDTO.setCelestialBody(celestialBodies);

        return celestialBodiesMessageDTO;
    }

    /**
     * returns the short currency code for a given currency name
     * @param currencyName
     * @return currency code
     */
    private String getCurrencyCodeForCurrencyName(String currencyName) {

        return switch (currencyName) {
            case "Euro" -> "EUR";
            case "Dollar" -> "USD";
            case "Pound" -> "GBP";
            case "Lira" -> "TRY";
            case "Yen" -> "JPY";
            default -> "Euro";
        };
    }

    /**
     * returns the numeric conversion rate between two currencies
     * @param currencyToConvertFrom
     * @param currencyToConvertTo
     * @return conversion rate
     */
    private double getConversionRateAsDouble(String currencyToConvertFrom, String currencyToConvertTo) {

        String currencyCodeToConvertFrom = getCurrencyCodeForCurrencyName(currencyToConvertFrom);
        String currencyCodeToConvertTo = getCurrencyCodeForCurrencyName(currencyToConvertTo);

        String composedApiUrlString = currencyApiUrl + currencyApiKey + "/pair/" + currencyCodeToConvertFrom + "/" + currencyCodeToConvertTo;

        String conversionRateAsJson = getConversionRatesAsJsonFromAPI(composedApiUrlString);
        double conversionRate = readConversionRateFromJson(conversionRateAsJson);

        return conversionRate;
    }

    /**
     * retrieves the current conversion rate from an external api
     * @param composedApiUrlString
     * @return conversion rates as json
     */
    private String getConversionRatesAsJsonFromAPI(String composedApiUrlString) {
        HttpURLConnection httpURLConnection;
        URL url;

        String conversionAsJson = "";

        try {
            url = new URL(composedApiUrlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                Scanner scan = new Scanner(url.openStream());
                while (scan.hasNext()) {
                    conversionAsJson = scan.nextLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return conversionAsJson;
    }

    /**
     * reads the numeric conversion rate from json
     * @param conversionRateAsJson
     * @return conversion rate
     */
    private double readConversionRateFromJson(String conversionRateAsJson) {
        JsonNode conversionNode;

        objectMapper = new ObjectMapper();

        try {
            conversionNode = new ObjectMapper().readValue(conversionRateAsJson, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        double conversionRate = conversionNode.path("conversion_rate").asDouble();

        return conversionRate;
    }

    /**
     * parses a json message containing a list of planetary systems to a dto
     * @param messageAsJson
     * @return planetarySystemsMessageDTO
     */
    public PlanetarySystemsMessageDTO parseMessageToPlanetarySystemsDTO(String messageAsJson) {
        PlanetarySystemsMessageDTO messageDTO;

        objectMapper = new ObjectMapper();

        try {
            messageDTO = objectMapper.readValue(messageAsJson, PlanetarySystemsMessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageDTO;
    }

    /**
     * parses a json message containing a list of celestial bodies to a dto
     * @param messageAsJson
     * @return celestialbodiesMessageDTO
     */
    public CelestialBodiesMessageDTO parseMessageToCelestialBodiesDTO(String messageAsJson) {
        CelestialBodiesMessageDTO messageDTO;

        objectMapper = new ObjectMapper();

        try {
            messageDTO = objectMapper.readValue(messageAsJson, CelestialBodiesMessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageDTO;
    }

    /**
     * parses a message dto containing planetary systems to json
     * @param planetarySystemsMessageDTO
     * @return message as json
     */
    public String parseMessagePlanetarySystemsDTOToJson(PlanetarySystemsMessageDTO planetarySystemsMessageDTO) {
        String messageAsJson;

        objectMapper = new ObjectMapper();

        try {
            messageAsJson = objectMapper.writeValueAsString(planetarySystemsMessageDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageAsJson;
    }

    /**
     * parses a message dto containing celestial bodies to json
     * @param celestialBodiesMessageDTO
     * @return message as json
     */
    public String parseMessageCelestialBodiesDTOToJson(CelestialBodiesMessageDTO celestialBodiesMessageDTO) {
        String messageAsJson;

        objectMapper = new ObjectMapper();

        try {
            messageAsJson = objectMapper.writeValueAsString(celestialBodiesMessageDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageAsJson;
    }
}
