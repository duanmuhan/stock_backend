package com.cgs.vo;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
public class StockAchievementGroupVO {
    private String date;
    private List<Pair<String,Long>> list;
}
