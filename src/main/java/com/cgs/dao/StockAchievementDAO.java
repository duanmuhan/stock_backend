package com.cgs.dao;

import com.cgs.entity.StockAchievement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAchievementDAO {

    String TABLE_NAME = " stock_achievement ";

    @Results(id="stockAchievement", value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockName",column = "stock_name"),
            @Result(property = "achievementType",column = "achievement_type"),
            @Result(property = "achievementTitle",column = "achievement_title"),
            @Result(property = "profileChangeRate",column = "profile_change_rate"),
            @Result(property = "profileLastYear",column = "profile_last_year"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Select("select * from " + TABLE_NAME + " order by profile_change_rate desc limit #{startIndex}, #{endIndex}")
    public List<StockAchievement> queryStockAchievementOrderByProfileChangeRate(@Param("startIndex") String startIndex,@Param("endIndex") String endIndex);
}
