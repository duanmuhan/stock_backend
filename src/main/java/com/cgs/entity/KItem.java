package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class KItem implements Serializable {
    private String stockId;
    private Double openPrice;
    private Double closePrice;
    private Double high;
    private Double low;
    private Long dealAmount;
    private Double dealCash;
    private Integer type;
    private String date;
}
