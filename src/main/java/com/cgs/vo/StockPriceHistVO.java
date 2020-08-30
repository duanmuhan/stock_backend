package com.cgs.vo;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
public class StockPriceHistVO {
    private String date;
    List<Pair<String,Long>> list;
}
