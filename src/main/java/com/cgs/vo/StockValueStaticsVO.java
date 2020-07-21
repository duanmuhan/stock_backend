package com.cgs.vo;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StockValueStaticsVO {
    String date;
    //市值区分 50亿 100亿 200亿 500亿 1000亿
    List<Pair<Integer,Double>> valuePairList;
}
