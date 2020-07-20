package com.cgs.dao;

import com.cgs.entity.PlateInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateInfoDAO {
    String TABLE_NAME = " plate_info ";
    String COLUMNS = " plate_id, plate_name, type, date ";

    @Select(" select * from " + TABLE_NAME + " where plate_id=#{plateId}")
    @Cacheable(value = "stock::plate",key = "#plateId")
    List<PlateInfo> queryPlateInfosByPlateId(@Param("plateId") String plateId);
}
