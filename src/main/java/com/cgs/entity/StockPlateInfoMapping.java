package com.cgs.entity;

import lombok.Data;

@Data
public class StockPlateInfoMapping {
    private Long id;
    private String stockId;
    private String stockName;
    private String plateId;
    private String plateName;
    private String date;
}
