package com.cgs.vo;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StockHolderRateVO {
    private String date;
    List<Pair<String,Long>> list;
}
