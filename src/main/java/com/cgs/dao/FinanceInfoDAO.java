package com.cgs.dao;

import com.cgs.entity.FinanceInfo;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceInfoDAO {

    String TABLE_NAME = "finance_info";
    String COLUMNS = " stock_id, basic_earning_per_common_share," +
            " deduction_of_non_eps, diluted_eps, net_asset_value_per_share, per_share_reserve, udpps," +
            " operating_cash_flow_per_share, gross_revenue, gross_profit, attributable_net_profit, deduct_non_net_profit, year_on_year_growth_of_total_operate_revenue, attributable_net_profit_increased_year_on_year, " +
            " deduction_of_non_net_profit_increased_year_on_year, attributable_net_profit_increases_on_arollingbasis, deduction_of_non_net_profit, weighted_return_on_equity, diluted_return_on_equity," +
            " diluted_return_on_total_assets, rate_of_gross_profit, rate_of_margin, net_margin, etr, items_received_in_advance, cash_flow_from_sales, operation_cash_flow, total_assets_turnover, days_sales_outstanding," +
            " days_in_inventory, debt, accrued_liabilities, current_ratio, quick_ratio, release_date, date ";

    @Results(id="financeInfo", value = {
            @Result(column = "stock_id", property = "stockId", javaType = String.class),
            @Result(column = "basic_earning_per_common_share", property = "basicEarningsPerCommonShare", javaType = String.class),
            @Result(column = "deduction_of_non_eps", property = "deductionOfNonEPS", javaType = String.class),
            @Result(column = "diluted_eps", property = "dilutedEPS", javaType = String.class),
            @Result(column = "net_asset_value_per_share", property = "netAssetValuePerShare", javaType = String.class),
            @Result(column = "per_share_reserve", property = "perShareReserve", javaType = String.class),
            @Result(column = "udpps", property = "UDPPS", javaType = String.class),
            @Result(column = "operating_cash_flow_per_share", property = "operatingCashFlowPerShare", javaType = String.class),
            @Result(column = "gross_revenue", property = "grossRevenue", javaType = String.class),
            @Result(column = "gross_profit", property = "grossProfit", javaType = String.class),
            @Result(column = "attributable_net_profit", property = "attributableNetProfit", javaType = String.class),
            @Result(column = "deduct_non_net_profit", property = "deductNonNetProfit", javaType = String.class),
            @Result(column = "year_on_year_growth_of_total_operate_revenue", property = "yearOnYearGrowthOfTotalOperatingRevenue", javaType = String.class),
            @Result(column = "attributable_net_profit_increased_year_on_year", property = "attributableNetProfitIncreasedYearOnYear", javaType = String.class),
            @Result(column = "deduction_of_non_net_profit_increased_year_on_year", property = "deductionOfNonNetProfitIncreasedYearOnYear", javaType = String.class),
            @Result(column = "attributable_net_profit_increases_on_arollingbasis", property = "attributableNetProfitIncreasesOnARollingBasis", javaType = String.class),
            @Result(column = "deduction_of_non_net_profit", property = "deductNonNetProfit", javaType = String.class),
            @Result(column = "weighted_return_on_equity", property = "weightedReturnOnEquity", javaType = String.class),
            @Result(column = "diluted_return_on_equity", property = "dilutedReturnOnEquity", javaType = String.class),
            @Result(column = "diluted_return_on_total_assets", property = "dilutedReturnOnTotalAssets", javaType = String.class),
            @Result(column = "rate_of_gross_profit", property = "rateOfGrossProfit", javaType = String.class),
            @Result(column = "rate_of_margin", property = "rateOfMargin", javaType = String.class),
            @Result(column = "net_margin", property = "netMargin", javaType = String.class),
            @Result(column = "etr", property = "ETR", javaType = String.class),
            @Result(column = "items_received_in_advance", property = "itemsReceivedInAdvance", javaType = String.class),
            @Result(column = "cash_flow_from_sales", property = "cashFlowFromSales", javaType = String.class),
            @Result(column = "operation_cash_flow", property = "operationCashFlow", javaType = String.class),
            @Result(column = "total_assets_turnover", property = "totalAssetsTurnover", javaType = String.class),
            @Result(column = "days_sales_outstanding", property = "daysSalesOutstanding", javaType = String.class),
            @Result(column = "days_in_inventory", property = "daysInInventory", javaType = String.class),
            @Result(column = "debt", property = "DEBT", javaType = String.class),
            @Result(column = "accrued_liabilities", property = "accruedLiabilities", javaType = String.class),
            @Result(column = "current_ratio", property = "currentRatio", javaType = String.class),
            @Result(column = "quick_ratio", property = "quickRatio", javaType = String.class),
            @Result(column = "release_date", property = "releaseDate", javaType = String.class),
            @Result(column = "date", property = "date", javaType = String.class)
    })

    @Select("select * from " + TABLE_NAME)
    @Cacheable(value = "stock::finance")
    public List<FinanceInfo> queryFinanceInfo();
}
