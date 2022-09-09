package com.currencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
public class CelestialBody implements Serializable {

    private UUID id;
    private String name;
    private int amount;
    private float price;
    private String type;
    private int orbital;
    private float radius;
    private float volume;
    private float mass;
    private float gravity;
    private float rotationVelocity;
    private float orbitalVelocity;
    private float surfaceTemperature;

    public CelestialBody(@JsonProperty("id") UUID id,
                         @JsonProperty("name") String name,
                         @JsonProperty("amount") int amount,
                         @JsonProperty("price") float price,
                         @JsonProperty("type") String type,
                         @JsonProperty("orbital") int orbital,
                         @JsonProperty("radius") float radius,
                         @JsonProperty("volume") float volume,
                         @JsonProperty("mass") float mass,
                         @JsonProperty("gravity") float gravity,
                         @JsonProperty("rotationVelocity") float rotationVelocity,
                         @JsonProperty("orbitalVelocity") float orbitalVelocity,
                         @JsonProperty("surfaceTemperature") float surfaceTemperature) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.orbital = orbital;
        this.radius = radius;
        this.volume = volume;
        this.mass = mass;
        this.gravity = gravity;
        this.rotationVelocity = rotationVelocity;
        this.orbitalVelocity = orbitalVelocity;
        this.surfaceTemperature = surfaceTemperature;
    }
}