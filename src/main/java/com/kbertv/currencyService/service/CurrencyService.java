package com.kbertv.currencyService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kbertv.currencyService.model.DTO.MessageDTO;
import com.kbertv.currencyService.model.PlanetarySystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class CurrencyService {
    @Value("${currencyapi.url}")
    private String currencyApiUrl;
    @Value("${currencyapi.key}")
    private String currencyApiKey;

    static ObjectMapper objectMapper;

    public MessageDTO convertCurrencyForMessage(MessageDTO messageDTO)
    {
        ArrayList<PlanetarySystem> products = messageDTO.getPlanetarySystems();
        String currencyToConvertFrom = messageDTO.getCurrencyToConvertFrom();
        String currencyToConvertTo = messageDTO.getCurrencyToConvertTo();

        double conversionRate = getConversionRate(currencyToConvertFrom, currencyToConvertTo);

        for (PlanetarySystem planetarySystem : products
        ) {
            float convertedPrice = (float) (planetarySystem.getPrice() * conversionRate);
            planetarySystem.setPrice(convertedPrice);
        }

        messageDTO.setPlanetarySystems(products);

        return messageDTO;
    }

    public MessageDTO parseMessageToDTO(String messageAsJson) {
        MessageDTO messageDTO;

        objectMapper = new ObjectMapper();

        try {
            messageDTO = objectMapper.readValue(messageAsJson, MessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageDTO;
    }

    public String parseMessageDTOToJson(MessageDTO message) {
        String messageAsJson;

        objectMapper = new ObjectMapper();

        try {
            messageAsJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageAsJson;
    }

    private String getCurrencyCode(String currencyName) {

        return switch (currencyName) {
            case "Euro" -> "EUR";
            case "Dollar" -> "USD";
            case "Pound" -> "GBP";
            case "Lira" -> "TRY";
            case "Yen" -> "JPY";
            default -> "";
        };
    }

    private double getConversionRate(String currencyToConvertFrom, String currencyToConvertTo) {

        String currencyCodeToConvertFrom = getCurrencyCode(currencyToConvertFrom);
        String currencyCodeToConvertTo = getCurrencyCode(currencyToConvertTo);

        String urlString = currencyApiUrl + currencyApiKey + "/pair/" + currencyCodeToConvertFrom + "/" + currencyCodeToConvertTo;

        String conversionAsJson = getConversionRateAsJsonFromAPI(urlString);
        double conversionRate = readConversionRateFromJson(conversionAsJson);

        return conversionRate;
    }

    private String getConversionRateAsJsonFromAPI(String urlString) {
        HttpURLConnection request;
        URL url;

        try {
            url = new URL(urlString);
            request = (HttpURLConnection) url.openConnection();
            request.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String conversionAsJson = "";

        try {
            if (request.getResponseCode() == 200) {
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

    private double readConversionRateFromJson(String conversionAsJson) {
        JsonNode conversionNode;

        objectMapper = new ObjectMapper();

        try {
            conversionNode = new ObjectMapper().readValue(conversionAsJson, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        double conversionRate = conversionNode.path("conversion_rate").asDouble();

        return conversionRate;
    }


}
