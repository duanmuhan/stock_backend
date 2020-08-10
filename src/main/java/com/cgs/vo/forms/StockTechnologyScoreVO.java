package com.cgs.vo.forms;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockTechnologyScoreVO implements Serializable {
    private String stockId;
    private String stockName;
    private Double score;
    private String releaseDate;
}
