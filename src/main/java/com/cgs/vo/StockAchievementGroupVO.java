package com.cgs.vo;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StockAchievementGroupVO {
    private String date;
    private List<Pair<String,Long>> list;
}
