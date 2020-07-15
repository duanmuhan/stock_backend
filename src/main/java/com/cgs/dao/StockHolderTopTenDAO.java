package com.cgs.dao;

import com.cgs.entity.StockHolderTopTen;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHolderTopTenDAO {

    String TABLE_NAME = " stock_holder_top_ten ";

    String COLUMNS = " stock_id, stock_holder_name, type_of_stock_holder, type_of_share," +
            " numbers_of_shares, rate, changes, change_rate, release_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockHolderName}, #{item.typesOfStockHolders}, #{item.typesOfShares}, #{item.numbersOfShares}, #{item.rate} " +
            ", #{item.changes}, #{item.changeRate} " +
            ", #{item.releaseDate})" +
            "</foreach>"+
            "</script>"})
    public void batchInsertStockHolderTopTen(List<StockHolderTopTen> list);
}
