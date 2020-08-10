package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockTechnology implements Serializable {
    private String stockId;
    private String type;
    private String special;
    private String queryStr;
    private String tag;
    private String descStr;
    private String releaseDate;
}
