package com.cgs.dao;

import com.cgs.entity.PlateInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateInfoDAO {
    String TABLE_NAME = " plate_info ";
    String COLUMNS = " plate_id, plate_name, type, date ";

    @Results( id = "resultMap",value = {
            @Result(property = "plateId",column = "plate_id"),
            @Result(property = "plateName",column = "plate_name"),
            @Result(property = "type",column = "type"),
            @Result(property = "date",column = "date")
    })
    @Select(" select * from " + TABLE_NAME + " where plate_id=#{plateId}")
    @Cacheable(value = "stock::plate",key = "#plateId")
    List<PlateInfo> queryPlateInfosByPlateId(@Param("plateId") String plateId);


    @ResultMap(value = "resultMap")
    @Select("<script>" +
            "select * from " + TABLE_NAME + "where plate_id in " +
            "<foreach collection ='plateIds' index='index' item='item' open='(' close=')' separator=','>" +
            "#{item} " +
            "</foreach>" +
            "</script>")
    @Cacheable(value = "stock::plate::list",key = "#plateIds")
    List<PlateInfo> batchQueryPlateInfosByPlateIds(@Param("plateIds") List<String> plateIds);
}
