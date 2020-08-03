package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockPlateInfoMapping implements Serializable {
    private String stockId;
    private String stockName;
    private String plateId;
    private String plateName;
    private String date;
}
