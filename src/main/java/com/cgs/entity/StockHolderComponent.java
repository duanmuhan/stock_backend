package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockHolderComponent implements Serializable {

    private String stockId;
    //机构类型
    private String organizationType;
    //持仓家数
    private String positions;
    //持仓股数(股)
    private String numberOfSharesHeld;
    //占流通股比例
    private String proportionOfTradableShares;
    //占总股本比例
    private String proportionInTotalEquity;
    private String releaseDate;
}
