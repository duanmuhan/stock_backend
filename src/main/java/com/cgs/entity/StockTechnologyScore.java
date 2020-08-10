package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockTechnologyScore implements Serializable {
    private String stockId;
    private String stockName;
    private Double score;
    private String releaseDate;
}
