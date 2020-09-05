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
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockName",column = "stock_name"),
            @Result(property = "plateId",column = "plate_id"),
            @Result(property = "plateName",column = "plate_name"),
            @Result(property = "date",column = "date")
    })
    @Select(" select * from " + TABLE_NAME + " where stock_id = #{stockId}")
    @Cacheable(value = "stock:plate", key = "#stockId")
    public List<StockPlateInfoMapping> queryPlateInfoMappingByStockId(@Param("stockId") String stockId);

    @Select(" select * from " + TABLE_NAME + " where plate_id = #{plateId} limit #{startIndex}, #{endIndex}")
    @ResultMap(value = "stockPlateInfoMapping")
    @Cacheable(value = "stock:plate", key = "#plateId + '-' + #startIndex + '-' + #endIndex")
    public List<StockPlateInfoMapping> queryPlateInfoMappingByPlateId(@Param("plateId") String plateId,@Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex);

    @Select(" select count(*) from " + TABLE_NAME + " where plate_id = #{plateId}")
    public Integer queryPlateInfoMappingCountByPlate(@Param("plateId") String plateId);

}
