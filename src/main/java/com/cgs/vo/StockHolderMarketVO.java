package com.cgs.vo;

import lombok.Data;

@Data
public class StockHolderMarketVO {
    private String stockId;
    private Double stockHolderConcentrationRate;
    private Double marketValue;
}
