package com.cgs.vo;

import lombok.Data;

@Data
public class LatestLimitUpVO {
    private String stockId;
    private String stockName;
    private String increaseRate;
    private String plate;
    private String date;
}
