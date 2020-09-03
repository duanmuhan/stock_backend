package com.cgs.dao;

import com.cgs.entity.AverageItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AverageDAO {

    String TABLE_NAME = " average_item ";

    String COLUMNS = " stock_id, price, type, date ";

    String REDIS_PREFIX="stock::average::";

    @Select("select * from " + TABLE_NAME + " where stock_id = #{stockId} order by date desc limit 100")
    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "price",column = "price"),
            @Result(property = "type",column = "type"),
            @Result(property = "date",column = "date"),
    })
    @Cacheable(value = REDIS_PREFIX + "queryAverageItemListByStockId" ,key = "#stockId + '-' + #type ")
    public List<AverageItem> queryAverageItemListByStockId( @Param("stockId") String stockId,@Param("type") Integer type);
}
