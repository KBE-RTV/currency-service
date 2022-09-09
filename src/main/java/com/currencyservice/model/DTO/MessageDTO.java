package com.currencyservice.model.DTO;

import com.currencyservice.model.PlanetarySystem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@ToString
public class MessageDTO {
    private UUID callId;

    private ArrayList<PlanetarySystem> products;

    private String currencyToConvertFrom;

    private String currencyToConvertTo;


    public MessageDTO(@JsonProperty("callId") UUID callId,
                      @JsonProperty("products") ArrayList<PlanetarySystem> products,
                      @JsonProperty("currencyToConvertFrom") String currencyToConvertFrom,
                      @JsonProperty("currencyToConvertTo") String currencyToConvertTo) {
        this.callId = callId;
        this.products = products;
        this.currencyToConvertFrom = currencyToConvertFrom;
        this.currencyToConvertTo = currencyToConvertTo;
    }
}
