package com.cgs.dao;

import com.cgs.entity.KItem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KItemDAO {

    String TABLE_NAME = " k_item ";

    String COLUMNS = " stock_id, open_price, close_price, high, low, deal_amount, date ";

    @Select("select * from" + TABLE_NAME + "where stock_id = #{stockId}")
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
    public List<KItem> queryKItemsbyStockId(@Param("stockId") String stockId);

    @Select("select * from " + TABLE_NAME + " group by stock_id order by date limit 0,1 " )
    @ResultMap(value = "resultMap")
    public List<KItem> queryLatestValue();
}
