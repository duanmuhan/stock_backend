package com.cgs.dao;

import com.cgs.entity.StockTechnology;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTechnologyDAO {
    String TABLE_NAME = " stock_technology ";
    String COLUMNS = "stock_id, type, special, query_str, tag, desc_str, release_date";

    @Results(id="stockPlateInfoMapping", value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockName",column = "stock_name"),
            @Result(property = "plateId",column = "plate_id"),
            @Result(property = "plateName",column = "plate_name"),
            @Result(property = "date",column = "date")
    })
    @Select(" select * from stock_technology INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_technology GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date")
    @Cacheable(value = "stock:latestStockTechnology")
    public List<StockTechnology> queryLatestStockTechnologyList();

    @Select("select * from stock_score INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_score GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date AND stock_id = #{stockId}")
    @Cacheable(value = "stock:latestStockTechnology",key = "#stockId")
    public StockTechnology queryStockTechnologyByStockId(@Param("stockId") String stockId);
}
