package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockAchievement implements Serializable {
    private String stockId;
    private String stockName;
    private String achievementType;
    private String achievementTitle;
    private String profileChangeRate;
    private String profileLastYear;
    private String releaseDate;
}
