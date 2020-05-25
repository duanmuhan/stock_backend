package com.cgs.entity;

import lombok.Data;

@Data
public class StockItem {
    private String stockId;
    private String exchangeId;
    private String name;
    private String listingDate;
    private Long updateTime;
}
