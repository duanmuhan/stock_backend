package com.cgs.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoDAO {

    String TABLE_NAME = "stock_info";

    String COLUMNS = " stock_id, total_transaction_amount, total_volume, total_share_capital, stock_circulation_share_capital, " +
            "total_market_value, flow_market_value, pe_ratio, average_turnover_rate, date ";

    @Select("select count(*) " + TABLE_NAME + " where total_market_value <= #{maxValue} and total_market_value >= #{minValue} and date= #{date}")
    @Cacheable(value = "stockInfo::value",key = " #maxValue + '-' #minValue + '-' + #date")
    public Integer queryStockInfoCountByValue(@Param("maxValue") Integer maxValue, @Param("minValue") Integer minValue , @Param("date") String date);

    @Select("select count(*) " + TABLE_NAME + " where total_market_value > #{value} and date= #{date}")
    @Cacheable(value = "stockInfo::value",key = " #value + '-' + #date")
    public Integer queryStockInfoCountLargerThanValue(@Param("value") Integer value, @Param("date") String date);

    @Select("select stock_id " + TABLE_NAME + " where total_market_value = #{maxValue} and total_market_value=#{minValue} and date= #{date}")
    @Cacheable(value = "stockInfo::stockId",key = " #value + '-' + #date")
    public List<String> queryStockIdByValueAndDate(@Param("maxValue") Integer value, @Param("minValue") Integer minValue, @Param("date") String date);
}