package com.kbertv.currencyService.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbertv.currencyService.model.PlanetarySystem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@ToString
public class MessageDTO {
    private UUID requestID;

    private ArrayList<PlanetarySystem> planetarySystems;

    private String currencyToConvertFrom;

    private String currencyToConvertTo;


    public MessageDTO(@JsonProperty("requestID") UUID callId,
                      @JsonProperty("planetarySystems") ArrayList<PlanetarySystem> products,
                      @JsonProperty("currencyToConvertFrom") String currencyToConvertFrom,
                      @JsonProperty("currencyToConvertTo") String currencyToConvertTo) {
        this.requestID = callId;
        this.planetarySystems = products;
        this.currencyToConvertFrom = currencyToConvertFrom;
        this.currencyToConvertTo = currencyToConvertTo;
    }
}
