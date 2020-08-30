package com.cgs.vo.news;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
public class StockNewsVO {
    private String title;
    private List<Pair<String,String>> platePairList;
    private String source;
    private String platform;
    private String releaseDate;
}
