package com.cgs.dao;

import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemDAO {

    String TABLE_NAME = " stock_item ";
    String COLUMNS = "stock_id, exchange_id, name, listing_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.exchangeId}, #{item.name} ,  #{item.listingDate} )" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertStockItem(List<StockItem> list);

    @Insert("insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values (#{item.stockId}, #{item.exchangeId}, #{item.name}, #{item.listingDate} )")
    public void insertStockItem(@Param("item") StockItem item);

    @Select("select stock_id as stockId, listing_date as listingDate, exchange_id as exchangeId, name as name  from " + TABLE_NAME)
    public List<StockItem> queryAllStockList();

    @Select("select stock_id as stockId, listing_date as listingDate, exchange_id as exchangeId, name as name  from " + TABLE_NAME + "where stock_id like #{stockId}%")
    public List<StockItem> queryStockItemsByStockId(@Param("stockId") String stockId);

    @Select("select stock_id as stockId, listing_date as listingDate, exchange_id as exchangeId, name as name  from " + TABLE_NAME + "where stock_id like #{stockName}%")
    public List<StockItem> queryStockItemsByStockName(@Param("stockName") String stockName);

    @Select("<script>" +
            "select stock_id as stockId, listing_date as listingDate, exchange_id as exchangeId, name as name  from " + TABLE_NAME + "where stock_id in" +
            "<foreach collections ='stockIdList' index='index' item='item' open='(' close=')' separator=','>" +
            "#{item} " +
            "</foreach>" +
            "</script>")
    public List<StockItem> queryStockListByStockIds(@Param("stockIdList") List<String> stockIdList);

}
