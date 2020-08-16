package com.cgs.vo;

import lombok.Data;

@Data
public class StockPriceVO {
    private String stockId;
    private String stockName;
    private Double price;
    private String releaseDate;
}
