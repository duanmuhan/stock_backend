package com.cgs.vo;

import lombok.Data;

@Data
public class StockOverViewVO {
    private String stockId;
    private Double price;
    private Long dealAmount;
    private Double dealCash;
}
