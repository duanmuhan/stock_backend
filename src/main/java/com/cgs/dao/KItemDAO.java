package com.cgs.dao;

import com.cgs.entity.KItem;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KItemDAO {

    String TABLE_NAME = " k_item ";

    String COLUMNS = " stock_id, open_price, close_price, high, low, deal_amount, date ";

    @Select("select * from" + TABLE_NAME + "where stock_id = #{stockId}  and type = #{type} order by date desc limit 100")
    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "openPrice",column = "open_price"),
            @Result(property = "closePrice",column = "close_price"),
            @Result(property = "high",column = "high"),
            @Result(property = "low",column = "low"),
            @Result(property = "dealAmount",column = "deal_amount"),
            @Result(property = "dealCash",column = "deal_cash"),
            @Result(property = "date",column = "date")
    })
    @Cacheable(value = "kitem",key = "#stockId + '-' +#type")
    public List<KItem> queryKItemsbyStockId(@Param("stockId") String stockId, @Param("type") Integer type);

    @Select("select * from" + TABLE_NAME + "where date = #{date}")
    @ResultMap(value = "resultMap")
    @Cacheable(value = "kitem",key = "#date")
    public List<KItem> queryKItemsByDate(@Param("date") String date);

    @Select("select * from k_item INNER JOIN (select MAX(date) as max_date, stock_id as stockId from k_item GROUP BY stock_id) A ON stock_id = A.stockId AND date = A.max_date and type = 1" )
    @ResultMap(value = "resultMap")
    @Cacheable(value = "kitem::latestPrice")
    public List<KItem> queryLatestValue();

    @Select("select * from k_item where date = (select max(date) from k_item where date not in (select max(date) from k_item)) and type = 1" )
    @ResultMap(value = "resultMap")
    @Cacheable(value = "kitem::secondLatestPrice")
    public List<KItem> querySecondLatestDate();

    @Select(" select * from " + TABLE_NAME + "where stock_id=#{stockId} and type = 1 order by date desc limit 2")
    @ResultMap(value = "resultMap")
    @Cacheable(value = "kitem:queryLatestValueByStockId",key = "#stockId")
    public List<KItem> queryLatestValueByStockId(@Param("stockId") String stockId);


    @Select(" <script> select * from k_item INNER JOIN (select MAX(date) as max_date, stock_id as stockId from k_item GROUP BY stock_id) A ON stock_id = A.stockId AND date = A.max_date and A.stockId in " +
            "<foreach collection ='stockIds' index='index' item='item' open='(' close=')' separator=','>" +
            "#{item} " +
            "</foreach>" +
            " </script>")
    @ResultMap(value = "resultMap")
    @Cacheable(value = "kitem:latest::list")
    public List<KItem> batchQueryLatestValueByStockIds(@Param("stockIds") List<String> stockId);
}
