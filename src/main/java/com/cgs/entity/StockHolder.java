package com.cgs.entity;

import lombok.Data;

@Data
public class StockHolder {

    private String stockId;
    //股东人数
    private String numberOfShareholders;
    //人均流通股
    private String perCapitaTradableShares;
    //较上期变化(%)
    private String lastChange;
    //筹码集中度
    private String stockConvergenceRate;
    //股价(元)
    private String price;
    //人均持股金额(元)
    private String perCapitaHoldingAmount;
    //前十大股东持股合计(%)
    private String topTenStockHolder;
    //前十大流通股东持股合计(%)
    private String topTenStockFlowHolder;
    //日期
    private String releaseDate;
}
