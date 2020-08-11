package com.cgs.vo.forms;

import lombok.Data;

@Data
public class StockTechnologyVO {
    private String stockId;
    private String stockName;
    private String buy;
    private String sell;
    private Integer buyCount;
    private Integer sellCount;
    private String event;
    private String releaseDate;

    public StockTechnologyVO(){
        this.buyCount = 0;
        this.sellCount = 0;
    }
}
