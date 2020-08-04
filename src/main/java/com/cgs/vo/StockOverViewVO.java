package com.cgs.vo;

import lombok.Data;

@Data
public class StockOverViewVO {
    private String stockId;
    private String stockName;
    private Double price;
    private String ricePercent;
    private Long dealAmount;
    private Double dealCash;
    private String priceRate;
    private String amountRate;
    private String dealCashRate;
    private String date;
}
