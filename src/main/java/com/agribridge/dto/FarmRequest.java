package com.agribridge.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FarmRequest {
    private String name;
    private String description;
    private String state;
    private String lga;
    private String townOrVillage;
    private String streetName;
    private double latitude;
    private double longitude;
}
