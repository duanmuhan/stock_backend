package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockItem implements Serializable {
    private String stockId;
    private String exchangeId;
    private String name;
    private String listingDate;
    private Long updateTime;
}
