package com.cgs.dao;

import com.cgs.entity.StockHolder;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHolderDAO {

    String TABLE_NAME = " stock_holder ";

    String COLUMNS = " stock_id, number_of_share_holder, per_capita_tradable_shares, last_change," +
            " stock_convergence_rate, price, per_capita_holding_amount, top_ten_stock_holder, top_ten_stock_flow_holder, release_date ";

    @Select("select * from stock_holder INNER JOIN (select MAX(release_date) as max_date, stock_id as stockId from stock_holder GROUP BY stock_id) A ON stock_id = A.stockId AND release_date = A.max_date")
    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "numberOfShareholders",column = "number_of_share_holder"),
            @Result(property = "perCapitaTradableShares",column = "per_capita_tradable_shares"),
            @Result(property = "lastChange",column = "last_change"),
            @Result(property = "stockConvergenceRate",column = "stock_convergence_rate"),
            @Result(property = "price",column = "price"),
            @Result(property = "perCapitaHoldingAmount",column = "per_capita_holding_amount"),
            @Result(property = "topTenStockHolder",column = "top_ten_stock_holder"),
            @Result(property = "topTenStockFlowHolder",column = "top_ten_stock_flow_holder"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Cacheable(value = "stock::holder")
    public List<StockHolder> queryNewestStockHolder();
}
