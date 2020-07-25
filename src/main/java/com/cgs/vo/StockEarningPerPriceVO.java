package com.cgs.vo;

import lombok.Data;

@Data
public class StockEarningPerPriceVO {
    private String stockId;
    private String stockName;
    private Double price;
    private Double earningsPerPrice;
    private String plate;
    private String date;
}
