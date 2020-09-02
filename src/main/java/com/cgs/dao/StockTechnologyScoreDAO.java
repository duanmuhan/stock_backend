package com.cgs.dao;

import com.cgs.entity.StockTechnologyScore;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTechnologyScoreDAO {

    String TABLE_NAME = " stock_score ";
    String COLUMNS = "stock_id, stock_name, score, release_date";

    @Results(id="stockTechnologyScore", value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockName",column = "stock_name"),
            @Result(property = "score",column = "score"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Select(" select * from stock_score INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_score GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date")
    public List<StockTechnologyScore> queryLatestStockTechnologyScoreList();

    @ResultMap(value = "stockTechnologyScore")
    @Cacheable(value = "stock::latestStockScore",key = "#stockId")
    @Select("select * from stock_score INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_score GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date AND stock_id = #{stockId}")
    public StockTechnologyScore queryStockTechnologyScoreByStockId(@Param("stockId") String stockId);


}
