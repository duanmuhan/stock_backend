package com.cgs.dao;

import com.cgs.entity.StockPlateInfoMapping;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPlateInfoMappingDAO {

    String TABLE_NAME = " plate_stock_mapping ";

    @Results(id="stockPlateInfoMapping", value = {
            @Result(column = "id", property = "id", javaType = Long.class),
            @Result(column = "stock_id", property = "stockId", javaType = String.class),
            @Result(column = "stock_name", property = "stockName", javaType = String.class),
            @Result(column = "plate_id", property = "plateId", javaType = String.class),
            @Result(column = "plate_name", property = "plateName", javaType = String.class),
            @Result(column = "date", property = "date", javaType = String.class)
    })
    @Select(" select * from " + TABLE_NAME + " where stock_id = #{stockId}")
    @Cacheable(value = "stock:plate", key = "#stockId")
    public List<StockPlateInfoMapping> queryPlateInfoByStockId(@Param("stockId") String stockId);

    @Select(" select * from " + TABLE_NAME + " where plate_id = #{plateId} ")
    @Cacheable(value = "stock:plate", key = "#plateId")
    public List<StockPlateInfoMapping> queryPlateInfoByPlateId(@Param("plateId") String plateId);

    @Select("<script>" +
            "select * from " + TABLE_NAME + "where stock_id in " +
            "<foreach collection ='stockList' index='index' item='item' open='(' close=')' separator=','>" +
            "#{item} " +
            "</foreach>" +
            "</script>")
    @ResultMap(value = "stockPlateInfoMapping")
    public List<StockPlateInfoMapping> queryPlateInfoByStockIds(@Param("stockList") List<String> stockList);
}
