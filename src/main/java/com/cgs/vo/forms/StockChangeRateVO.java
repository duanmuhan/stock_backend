package com.cgs.vo.forms;

import lombok.Data;

import java.io.Serializable;


@Data
public class StockChangeRateVO implements Serializable {
    private String stockId;
    private String stockName;
    private Double price;
    private String changeRate;
    private String date;
}
