package com.kbertv.currencyService.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbertv.currencyService.model.CelestialBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@ToString
public class CelestialBodiesMessageDTO {
    private UUID requestID;

    private ArrayList<CelestialBody> celestialBody;

    private String currencyToConvertFrom;

    private String currencyToConvertTo;


    public CelestialBodiesMessageDTO(@JsonProperty("requestID") UUID requestID,
                                     @JsonProperty("celestialBody") ArrayList<CelestialBody> celestialBody,
                                     @JsonProperty("currencyToConvertFrom") String currencyToConvertFrom,
                                     @JsonProperty("currencyToConvertTo") String currencyToConvertTo) {
        this.requestID = requestID;
        this.celestialBody = celestialBody;
        this.currencyToConvertFrom = currencyToConvertFrom;
        this.currencyToConvertTo = currencyToConvertTo;
    }
}
