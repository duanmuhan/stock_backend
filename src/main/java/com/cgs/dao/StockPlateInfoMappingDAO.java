package com.cgs.dao;

import com.cgs.entity.StockPlateInfoMapping;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPlateInfoMappingDAO {

    String TABLE_NAME = "plate_stock_mapping";
    String COLUMNS = " stock_id, stock_name, plate_id, plate_name, date ";

    @Results(id="stockPlateInfoMapping", value = {
            @Result(column = "id", property = "id", javaType = Long.class),
            @Result(column = "stock_id", property = "stockId", javaType = String.class),
            @Result(column = "stock_name", property = "stockName", javaType = String.class),
            @Result(column = "plate_id", property = "plateId", javaType = String.class),
            @Result(column = "plate_name", property = "plateName", javaType = String.class),
            @Result(column = "date", property = "date", javaType = String.class)
    })
    @Select(" select * from " + TABLE_NAME + " where stock_id = #{stockId}")
    public List<StockPlateInfoMapping> queryPlateInfoByStockId(@Param("stockId") String stockId);

    @Select(" select * from " + TABLE_NAME + " where plate_id = #{plateId} ")
    public List<StockPlateInfoMapping> queryPlateInfoByPlateId(@Param("plateId") String plateId);
}
