package com.currencyservice.service;

import com.currencyservice.model.DTO.MessageDTO;
import com.currencyservice.model.PlanetarySystem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String CURRENCY_API_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String CURRENCY_API_KEY = "5fc0b4d419ab7e3d96135d02";
    static ObjectMapper objectMapper = new ObjectMapper();

    public static MessageDTO convertCurrencyForMessage(MessageDTO messageDTO)
    {
        ArrayList<PlanetarySystem> products = messageDTO.getProducts();
        String currencyToConvertFrom = messageDTO.getCurrencyToConvertFrom();
        String currencyToConvertTo = messageDTO.getCurrencyToConvertTo();

        double conversionRate = getConversionRate(currencyToConvertFrom, currencyToConvertTo);

        for (PlanetarySystem planetarySystem : products
        ) {
            float convertedPrice = (float) (planetarySystem.getPrice() * conversionRate);
            planetarySystem.setPrice(convertedPrice);
        }

        messageDTO.setProducts(products);

        return messageDTO;
    }

    public static MessageDTO parseMessageToDTO(String messageAsJson) {
        MessageDTO messageDTO;

        try {
            messageDTO = objectMapper.readValue(messageAsJson, MessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageDTO;
    }

    public static String parseMessageDTOToJson(MessageDTO message) {
        String messageAsJson;

        try {
            messageAsJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return messageAsJson;
    }

    private static String getCurrencyCode(String currencyName) {

        return switch (currencyName) {
            case "Euro" -> "EUR";
            case "Dollar" -> "USD";
            case "Pound" -> "GBP";
            case "Lira" -> "TRY";
            case "Yen" -> "JPY";
            default -> "";
        };
    }

    private static double getConversionRate(String currencyToConvertFrom, String currencyToConvertTo) {

        String currencyCodeToConvertFrom = getCurrencyCode(currencyToConvertFrom);
        String currencyCodeToConvertTo = getCurrencyCode(currencyToConvertTo);

        String urlString = CURRENCY_API_URL + CURRENCY_API_KEY + "/pair/" + currencyCodeToConvertFrom + "/" + currencyCodeToConvertTo;

        String conversionAsJson = getConversionRateAsJsonFromAPI(urlString);
        double conversionRate = readConversionRateFromJson(conversionAsJson);

        return conversionRate;
    }

    private static String getConversionRateAsJsonFromAPI(String urlString) {
        HttpURLConnection request;
        URL url;

        try {
            url = new URL(urlString);
            request = (HttpURLConnection) url.openConnection();
            request.connect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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

    private static double readConversionRateFromJson(String conversionAsJson) {
        JsonNode conversionNode;

        try {
            conversionNode = new ObjectMapper().readValue(conversionAsJson, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        double conversionRate = conversionNode.path("conversion_rate").asDouble();

        return conversionRate;
    }


}
