package com.cgs.vo;

import lombok.Data;

@Data
public class StockHolderValueVO {
    private String stockId;
    private String stockName;
    private String rate;
    private String stockPrice;
    private String releaseDate;
}
