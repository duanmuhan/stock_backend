package com.cgs.vo.forms;

import lombok.Data;

@Data
public class StockPeriodChangeRateVO {
    private String stockId;
    private String stockName;
    private Double price;
    private String changeRate;
    private String toDate;
    private String fromDate;
}
