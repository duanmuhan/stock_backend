package com.cgs.dao;

import com.cgs.entity.StockAchievement;
import com.cgs.vo.forms.StockAchievementVO;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAchievementDAO {

    String TABLE_NAME = " stock_achievement ";

    @Results(id="stockAchievement", value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockName",column = "stock_name"),
            @Result(property = "achievementType",column = "achievement_type"),
            @Result(property = "achievementTitle",column = "achievement_abstract"),
            @Result(property = "profileChangeRate",column = "profit_change_rate"),
            @Result(property = "profileLastYear",column = "profit_last_year"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Select("select * from " + TABLE_NAME  + " where release_date>=#{date}" + " order by profit_change_rate desc limit #{startIndex}, #{endIndex}")
    public List<StockAchievement> queryStockAchievementOrderByProfileChangeRate(@Param("date") String date,@Param("startIndex") Integer startIndex,@Param("endIndex") Integer endIndex);

    @ResultMap(value = "stockAchievement")
    @Cacheable(value = "stock::achievement::date",key = "#date")
    @Select("select * from " + TABLE_NAME  + " where release_date>=#{date}")
    public List<StockAchievement> queryStockAchievement(@Param("date") String date);

    @ResultMap(value = "stockAchievement")
    @Cacheable(value = "stock::achievement::date",key = "#type + '-' + #date")
    @Select("select * from " + TABLE_NAME  + " where release_date>=#{date} and achievement_type = #{type}")
    public List<StockAchievement> queryStockAchievementByType(@Param("date") String date, @Param("type") String type);
}
