package com.cgs.vo;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StockPriceHistVO {
    private String date;
    List<Pair<String,Integer>> list;
}
