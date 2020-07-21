package com.cgs.entity;

import lombok.Data;

@Data
public class StockInfo {

    private String stockId;
    //总成交金额
    private Double totalTransactionAmount;
    //总成交量
    private Double totalVolume;
    //股票总股本
    private Double totalShareCapital;
    //股票流通股本
    private Double stockCirculationShareCapital;
    //股票总市值
    private Double totalMarketValue;
    //股票流通市值
    private Double flowMarketValue;
    //平均市盈率
    private Double peRatio;
    //平均换手率
    private Double averageTurnoverRate;
    private String date;
}
