package com.cgs.dao;

import com.cgs.entity.PolicyInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDAO {

    String TABLE_NAME = "policy_table";

    String COLUMNS = " title , target_plate , target_plate_id, source , plateform, release_date";

    @Select("select * from" + TABLE_NAME + "where release_date >= #{releaseDate} order by date desc ")
    @Results( id = "resultMap",value = {
            @Result(property = "title",column = "title"),
            @Result(property = "target_plate",column = "targetPlate"),
            @Result(property = "target_plate_id",column = "targetPlateId"),
            @Result(property = "source",column = "source"),
            @Result(property = "plateform",column = "platform"),
            @Result(property = "release_date",column = "release_date"),
    })
    @Cacheable(value = "news",key = "#releaseDate")
    public List<PolicyInfo> queryNewsListBeforeDate(@Param("releaseDate") String releaseDate);
}
