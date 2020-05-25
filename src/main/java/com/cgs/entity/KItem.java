package com.cgs.entity;

import lombok.Data;

@Data
public class KItem {
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
