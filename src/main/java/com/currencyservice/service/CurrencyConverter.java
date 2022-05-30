package com.currencyservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static String CURRENCY_API_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String CURRENCY_API_KEY = "84955e3a1e2709227eb5c3ec";

    public static double convertCurrency(double amount, String currencyToConvertFrom, String currencyToConvertTo) {
        String currencyFromCode = getCurrencyCode(currencyToConvertFrom);
        String currencyToCode = getCurrencyCode(currencyToConvertTo);

        System.out.println("currencyToConvertFrom: " + currencyFromCode + "\n" + "currencyToConvertTo: " + currencyToCode);

        double conversionRate = getConversionRateFromAPI(currencyFromCode, currencyToCode);

        double convertedAmount = amount * conversionRate;
        return convertedAmount;
    }

    private static String getCurrencyCode(String currency) {
        String currencyCode = switch (currency) {
            case "Euro" -> "EUR";
            case "Dollar" -> "USD";
            case "Pound" -> "GBP";
            case "Lira" -> "TRY";
            case "Yen" -> "JPY";
            default -> "";
        };

        return currencyCode;
    }

    private static double getConversionRateFromAPI(String currencyFromCode, String currencyToCode) {
        String urlString = CURRENCY_API_URL + CURRENCY_API_KEY + "/pair/" + currencyFromCode + "/" + currencyToCode;

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

        String apiCallResultString = "";

        try {
            if (request.getResponseCode() == 200) {
                Scanner scan = new Scanner(url.openStream());
                while (scan.hasNext()) {
                    apiCallResultString = scan.nextLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectNode conversionNode;
        try {
            conversionNode = new ObjectMapper().readValue(apiCallResultString, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        double conversionRate = conversionNode.path("conversion_rate").asDouble();
        String baseCode = conversionNode.get("base_code").asText();

        System.out.println("conversion_rate: " + conversionRate);

        return conversionRate;
    }

}
