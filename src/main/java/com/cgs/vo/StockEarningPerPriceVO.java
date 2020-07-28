package com.cgs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockEarningPerPriceVO implements Serializable {
    private String stockId;
    private String stockName;
    private Double price;
    private Double earningsPerPrice;
    private String plate;
    private String date;
}
