package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AverageItem implements Serializable {
    private String stockId;
    private Double price;
    private Integer type;
    private String date;
}
