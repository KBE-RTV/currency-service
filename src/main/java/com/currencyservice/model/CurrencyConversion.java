package com.currencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public class CurrencyConversion implements Serializable {

    int id;
    ArrayList<Double> amountList;
    String currencyToConvertFrom;
    String currencyToConvertTo;

    public CurrencyConversion(@JsonProperty("id") int id,
                              @JsonProperty("amount") ArrayList<Double> amountList,
                              @JsonProperty("currencyToConvertFrom") String currencyToConvertFrom,
                              @JsonProperty("currencyToConvertTo") String currencyToConvertTo) {
        this.amountList = amountList;
        this.currencyToConvertFrom = currencyToConvertFrom;
        this.currencyToConvertTo = currencyToConvertTo;
    }
}
