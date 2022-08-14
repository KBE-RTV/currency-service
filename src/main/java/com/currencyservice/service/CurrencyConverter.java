package com.currencyservice.service;

import com.currencyservice.model.CurrencyConversion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String CURRENCY_API_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String CURRENCY_API_KEY = "84955e3a1e2709227eb5c3ec";
    static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertAmounts(String conversionJson) {

        ObjectNode conversionNode;
        try {
            conversionNode = objectMapper.readValue(conversionJson, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode arrayNode = conversionNode.get("amountList");

        ObjectReader reader = objectMapper.readerFor(new TypeReference<List<Double>>() {
        });

        ArrayList<Double> amountList;
        try {
            amountList = reader.readValue(arrayNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String currencyToConvertFrom = conversionNode.get("currencyToConvertFrom").asText();
        String currencyToConvertTo = conversionNode.get("currencyToConvertTo").asText();

        System.out.println("Received: \n"
                + "amount: " + amountList + "\n"
                + "currencyToConvertFrom: " + currencyToConvertFrom + "\n"
                + "currencyToConvertTo: " + currencyToConvertTo + "\n");

        String currencyFromCode = getCurrencyCode(currencyToConvertFrom);
        String currencyToCode = getCurrencyCode(currencyToConvertTo);

        System.out.println("currencyToConvertFrom: " + currencyFromCode + "\n" + "currencyToConvertTo: " + currencyToCode);

        double conversionRate = getConversionRate(currencyFromCode, currencyToCode);

        ArrayList<Double> convertedAmountsList = new ArrayList<>();

        for (double amount : amountList
        ) {
            double convertedAmount = amount * conversionRate;
            convertedAmountsList.add(convertedAmount);
        }

        String convertedAmountsAsJson;

        CurrencyConversion convertedAmountsAsConversion = new CurrencyConversion(convertedAmountsList, currencyToConvertFrom, currencyToConvertTo);

        try {
            convertedAmountsAsJson = objectMapper.writeValueAsString(convertedAmountsAsConversion);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return convertedAmountsAsJson;
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

    private static double getConversionRate(String currencyCodeToConvertFrom, String currencyCodeToConvertTo) {
        String urlString = CURRENCY_API_URL + CURRENCY_API_KEY + "/pair/" + currencyCodeToConvertFrom + "/" + currencyCodeToConvertTo;

        String conversionAsJson = getConversionRateAsJsonFromAPI(urlString);

        double conversionRate = readConversionRateFromJson(conversionAsJson);

        System.out.println("conversion_rate: " + conversionRate);

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
