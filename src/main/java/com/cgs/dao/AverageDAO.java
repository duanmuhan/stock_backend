package com.cgs.dao;

import com.cgs.entity.AverageItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AverageDAO {

    String TABLE_NAME = " average_item ";

    String COLUMNS = " stock_id, price, type, date ";

    @Select("select * from " + TABLE_NAME + " where stock_id = #{stockId}")
    public List<AverageItem> queryAverageItemListByStockId( @Param("stockId") String stockId);
}
