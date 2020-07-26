package com.cgs.vo.forms;

import lombok.Data;


@Data
public class StockChangeRateVO {
    private String stockId;
    private String stockName;
    private String changeRate;
    private String plate;
    private String date;
}
