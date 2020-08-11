package com.cgs.dao;

import com.cgs.entity.StockTechnology;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTechnologyDAO {
    String TABLE_NAME = " stock_technology ";
    String COLUMNS = "stock_id, type, special, query_str, tag, desc_str, release_date";

    @Results(id="stockTechnology", value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "type",column = "type"),
            @Result(property = "special",column = "special"),
            @Result(property = "queryStr",column = "query_str"),
            @Result(property = "tag",column = "tag"),
            @Result(property = "descStr",column = "desc_str"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Select(" select * from stock_technology INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_technology GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date")
    @Cacheable(value = "stock:latestStockTechnology")
    public List<StockTechnology> queryLatestStockTechnologyList();

    @ResultMap(value = "stockTechnology")
    @Select("select * from stock_score INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_score GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date AND stock_id = #{stockId}")
    @Cacheable(value = "stock:latestStockTechnology",key = "#stockId")
    public StockTechnology queryStockTechnologyByStockId(@Param("stockId") String stockId);
}
