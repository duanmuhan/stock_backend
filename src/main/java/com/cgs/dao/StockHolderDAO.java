package com.cgs.dao;

import com.cgs.entity.StockHolder;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHolderDAO {

    String TABLE_NAME = " stock_holder ";

    String COLUMNS = " stock_id, number_of_share_holder, per_capita_tradable_shares, last_change," +
            " stock_convergence_rate, price, per_capita_holding_amount, top_ten_stock_holder, top_ten_stock_flow_holder, release_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "( #{item.stockId}, #{item.numberOfShareholders}, #{item.perCapitaTradableShares} ,  #{item.lastChange}" +
            ", #{item.stockConvergenceRate},  #{item.price} " +
            ", #{item.perCapitaHoldingAmount},  #{item.topTenStockHolder} " +
            ", #{item.topTenStockFlowHolder},  #{item.releaseDate} " +
            ")" +
            "</foreach>"+
            "</script>"})
    public void batchInsertStockHolder(List<StockHolder> list);
}
