package com.cgs.dao;

import com.cgs.entity.StockInfo;
import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoDAO {

    String TABLE_NAME = "stock_info";

    String COLUMNS = " stock_id, total_transaction_amount, total_volume, total_share_capital, stock_circulation_share_capital, " +
            "total_market_value, flow_market_value, pe_ratio, average_turnover_rate, date ";


    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "totalTransactionAmount",column = "total_transaction_amount"),
            @Result(property = "totalVolume",column = "total_volume"),
            @Result(property = "totalShareCapital",column = "total_share_capital"),
            @Result(property = "stockCirculationShareCapital",column = "stock_circulation_share_capital"),
            @Result(property = "totalMarketValue",column = "total_market_value"),
            @Result(property = "flowMarketValue",column = "flow_market_value"),
            @Result(property = "peRatio",column = "pe_ratio"),
            @Result(property = "averageTurnoverRate",column = "average_turnover_rate"),
            @Result(property = "date",column = "date")
    })
    @Select("select * from stock_info INNER JOIN (select MAX(date) as max_date, stock_id as stockId from stock_info GROUP BY stock_id) A ON stock_id = A.stockId AND date = A.max_date" )
    @Cacheable(value = "stockInfo:queryLatestStockInfoByStockId",key = "#stockId")
    public StockInfo queryLatestStockInfoByStockId(@Param("stockId") String stockId);

    @Select("select count from (select count(*) as count,date from stock_info where total_market_value <= #{maxValue} and total_market_value > #{minValue} group by date) A INNER JOIN (select MAX(date) as max_date from stock_info ) B on A.date = B.max_date")
    @Cacheable(value = "stockInfo::queryStockInfoCountByValue",key = "#maxValue + '-'+ #minValue")
    public Integer queryStockInfoCountByValue(@Param("minValue") Integer minValue, @Param("maxValue") Integer maxValue);

    @Select("select * from (select count(*) as count,date from stock_info where total_market_value <= #{maxValue} and total_market_value > #{minValue} group by date) A INNER JOIN (select MAX(date) as max_date from stock_info ) B on A.date = B.max_date")
    @Cacheable(value = "stockInfo::queryStockInfoListByValue",key = "#maxValue + '-'+ #minValue")
    public List<StockItem> queryStockInfoListByValue(@Param("minValue") Integer minValue, @Param("maxValue") Integer maxValue);

    @Select("select count from (select count(*) as count,date from stock_info where total_market_value > #{value} group by date) A INNER JOIN (select MAX(date) as max_date from stock_info ) B on A.date = B.max_date")
    @Cacheable(value = "stockInfo::value",key = "#value")
    public Integer queryStockInfoCountLargerThanValue(@Param("value") Integer value);

    @Select("select stock_id from " + TABLE_NAME + " where total_market_value = #{maxValue} and total_market_value=#{minValue} and date= #{date}")
    @Cacheable(value = "stockInfo::stockId",key = "#value + '-' + #date")
    public List<String> queryStockIdByValueAndDate(@Param("minValue") Integer minValue, @Param("maxValue") Integer maxValue, @Param("date") String date);
}