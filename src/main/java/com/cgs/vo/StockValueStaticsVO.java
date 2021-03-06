package com.cgs.vo;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
public class StockValueStaticsVO {

    String date;
    //市值区分 10亿 50亿 100亿 200亿 500亿 1000亿 2000亿 5000亿 1w亿
    List<Pair<Integer,Integer>> valuePairList;
}
