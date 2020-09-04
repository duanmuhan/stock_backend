package com.cgs.dao;

import com.cgs.entity.StockMoodIndex;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMoodIndexFetchDAO {

    String TABLE_NAME = " stock_mood_index ";
    String COLUMNS = "stock_increase_rate, stock_point, stock_total_count, stock_increase_count, release_date ";

    @Results( id = "resultMap",value = {
            @Result(property = "stockIncreaseRate",column = "stock_increase_rate"),
            @Result(property = "stockPoint",column = "stock_point"),
            @Result(property = "stockTotalCount",column = "stock_total_count"),
            @Result(property = "stockIncreaseCount",column = "stock_increase_count"),
            @Result(property = "releaseDate",column = "release_date"),
    })
    @Select("select * from " + TABLE_NAME + " limit 100 ")
    @Cacheable(value = "stock::mood::batchQueryStockMoodIndex")
    public List<StockMoodIndex> batchQueryStockMoodIndex();
}
