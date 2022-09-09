package com.currencyservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@ToString
public class PlanetarySystem implements Serializable {

    private UUID id;
    private String name;
    private String owner;
    private ArrayList<CelestialBody> celestialBodies;
    private float price;


    public PlanetarySystem(@JsonProperty("id") UUID id,
                           @JsonProperty("name") String name,
                           @JsonProperty("owner") String owner,
                           @JsonProperty("celestialBodies") ArrayList<CelestialBody> celestialBodies,
                           @JsonProperty("price") float price) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.celestialBodies = celestialBodies;
        this.price = price;
    }
}
