package com.cgs.vo.forms;

import lombok.Data;

@Data
public class StockTechnologyVO {
    private String stockId;
    private String stockName;
    private String buy;
    private String sell;
    private String buyCount;
    private String sellCount;
    private String releaseDate;
}
