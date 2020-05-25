package com.cgs.entity;

import lombok.Data;

@Data
public class FinanceInfo {
    //股票id
    private String stockId;
    //基本每股收益
    private String basicEarningsPerCommonShare;
    //扣非每股收益
    private String deductionOfNonEPS;
    //稀释每股收益
    private String dilutedEPS;
    //每股净资产
    private String netAssetValuePerShare;
    //每股公积金
    private String perShareReserve;
    //每股未分配利润
    private String UDPPS;
    //每股经营现金流
    private String operatingCashFlowPerShare;
    //营业总收入
    private String grossRevenue;
    //毛利润
    private String grossProfit;
    //归属净利润
    private String attributableNetProfit;
    //扣非净利润
    private String deductNonNetProfit;
    //营业总收入同比增长
    private String yearOnYearGrowthOfTotalOperatingRevenue;
    //归属净利润同比增长
    private String attributableNetProfitIncreasedYearOnYear;
    //扣非净利润同比增长
    private String deductionOfNonNetProfitIncreasedYearOnYear;
    //营业总收入滚动环比增长
    private String attributableNetProfitIncreasesOnARollingBasis;
    //归属净利润滚动环比增长
    private String deductionOfNonNetProfit;
    //扣非净利润滚动环比增长
    private String weightedReturnOnEquity;
    //加权净资产收益率
    private String dilutedReturnOnEquity;
    //摊薄净资产收益率
    private String dilutedReturnOnTotalAssets;
    //摊薄总资产收益率
    private String rateOfGrossProfit;
    //毛利率
    private String rateOfMargin;
    //净利率
    private String netMargin;
    //实际税率
    private String ETR;
    //预收款/营业收入
    private String itemsReceivedInAdvance;
    //销售现金流/营业收入
    private String cashFlowFromSales;
    //经营现金流/营业收入
    private String operationCashFlow;
    //总资产周转率(次)
    private String totalAssetsTurnover;
    //应收账款周转天数(天)
    private String daysSalesOutstanding;
    //存货周转天数(天)
    private String daysInInventory;
    //资产负债率(%)
    private String DEBT;
    //流动负债/总负债(%)
    private String accruedLiabilities;
    //流动比率
    private String currentRatio;
    //速动比率
    private String quickRatio;
    //发布日期
    private String releaseDate;
    //生成日期
    private String date;
}
