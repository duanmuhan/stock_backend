package com.cgs.dao;

import com.cgs.entity.StockItem;
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

    @Select("select count from (select count(*) as count,date from stock_info where total_market_value <= #{maxValue} and total_market_value > #{minValue} group by date) A INNER JOIN (select MAX(date) as max_date from stock_info ) B on A.date = B.max_date")
    @Cacheable(value = "stockInfo::value::queryStockInfoCountByValue",key = "#maxValue + '-'+ #minValue")
    public Integer queryStockInfoCountByValue(@Param("minValue") Integer minValue, @Param("maxValue") Integer maxValue);

    @Select("select * from (select count(*) as count,date from stock_info where total_market_value <= #{maxValue} and total_market_value > #{minValue} group by date) A INNER JOIN (select MAX(date) as max_date from stock_info ) B on A.date = B.max_date")
    @Cacheable(value = "stockInfo::value::queryStockInfoListByValue",key = "#maxValue + '-'+ #minValue")
    public List<StockItem> queryStockInfoListByValue(@Param("minValue") Integer minValue, @Param("maxValue") Integer maxValue);

    @Select("select count from (select count(*) as count,date from stock_info where total_market_value > #{value} group by date) A INNER JOIN (select MAX(date) as max_date from stock_info ) B on A.date = B.max_date")
    @Cacheable(value = "stockInfo::value",key = "#value")
    public Integer queryStockInfoCountLargerThanValue(@Param("value") Integer value);

    @Select("select stock_id from " + TABLE_NAME + " where total_market_value = #{maxValue} and total_market_value=#{minValue} and date= #{date}")
    @Cacheable(value = "stockInfo::stockId",key = "#value + '-' + #date")
    public List<String> queryStockIdByValueAndDate(@Param("minValue") Integer minValue, @Param("maxValue") Integer maxValue, @Param("date") String date);
}