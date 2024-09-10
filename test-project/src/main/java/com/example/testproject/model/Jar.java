package com.example.testproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Jar {
    private List<Cucumber> cucumbers;
    private BigDecimal maxVolume;
}

