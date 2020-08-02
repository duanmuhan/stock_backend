package com.cgs.vo.news;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StockNewsVO {
    private String title;
    private List<Pair<String,String>> platePairList;
    private String source;
    private String platform;
    private String releaseDate;
}
