package com.cgs.vo;

import lombok.Data;

import java.util.Map;

@Data
public class StockAchievementGroupVO {
    private String date;
    private Map<String,Long> groupMap;
}
