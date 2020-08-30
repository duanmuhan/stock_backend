package com.cgs.vo;

import org.springframework.data.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StockHolderRateVO {
    private String date;
    List<Pair<String,Long>> list;
}
