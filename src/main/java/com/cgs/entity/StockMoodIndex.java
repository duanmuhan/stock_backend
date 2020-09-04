package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockMoodIndex implements Serializable {

    private String stockIncreaseRate;
    private Double stockPoint;
    private Integer stockTotalCount;
    private Integer stockIncreaseCount;
    private String releaseDate;
}