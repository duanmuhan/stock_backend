package com.cgs.dao;

import com.cgs.entity.PolicyInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDAO {

    String TABLE_NAME = " policy_table ";

    String COLUMNS = " title , target_plate , target_plate_id, source , plateform, release_date";

    @Results( id = "resultMap",value = {
            @Result(property = "title",column = "title"),
            @Result(property = "targetPlate",column = "target_plate"),
            @Result(property = "targetPlateId",column = "target_plate_id"),
            @Result(property = "source",column = "source"),
            @Result(property = "platform",column = "platform"),
            @Result(property = "release_date",column = "release_date"),
    })
    @Cacheable(value = "news",key = "#releaseDate")
    @Select("select * from" + TABLE_NAME + "where order by id desc limit 50 ")
    public List<PolicyInfo> queryNewsListBeforeDate(@Param("releaseDate") String releaseDate);
}
