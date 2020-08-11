package com.cgs.vo.forms;

import lombok.Data;

@Data
public class StockTechnologyVO {
    private String stockId;
    private String type;
    private String special;
    private String queryStr;
    private String tag;
    private String descStr;
    private String releaseDate;
}
