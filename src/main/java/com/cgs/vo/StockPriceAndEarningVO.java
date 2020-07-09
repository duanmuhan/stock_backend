package com.cgs.vo;

import lombok.Data;

@Data
public class StockPriceAndEarningVO {
    private String stockId;
    private Double price;
    private Double basicEarningsPerCommonShare;
}
