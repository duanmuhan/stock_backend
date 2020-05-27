package com.cgs.entity;

import lombok.Data;

@Data
public class AverageItem {
    private String stockId;
    private Double price;
    private Integer type;
    private String date;
}
