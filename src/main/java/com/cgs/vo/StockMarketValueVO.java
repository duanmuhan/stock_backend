package com.cgs.vo;

import lombok.Data;

@Data
public class StockMarketValueVO {
    private String stockId;
    private String stockName;
    private String marketValue;
    private Double price;
    private String releaseDate;
}
