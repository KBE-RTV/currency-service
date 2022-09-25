package com.kbertv.currencyService.model.dto;

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
public class PlanetarySystemsMessageDTO {
    private UUID requestID;

    private ArrayList<PlanetarySystem> planetarySystems;

    private String currencyToConvertFrom;

    private String currencyToConvertTo;


    public PlanetarySystemsMessageDTO(@JsonProperty("requestID") UUID requestID,
                                      @JsonProperty("planetarySystems") ArrayList<PlanetarySystem> planetarySystems,
                                      @JsonProperty("currencyToConvertFrom") String currencyToConvertFrom,
                                      @JsonProperty("currencyToConvertTo") String currencyToConvertTo) {
        this.requestID = requestID;
        this.planetarySystems = planetarySystems;
        this.currencyToConvertFrom = currencyToConvertFrom;
        this.currencyToConvertTo = currencyToConvertTo;
    }
}
