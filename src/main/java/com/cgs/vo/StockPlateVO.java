package com.cgs.vo;

import com.cgs.entity.PlateInfo;
import com.cgs.entity.StockPlateInfoMapping;
import lombok.Data;

import java.util.List;

@Data
public class StockPlateVO {
    private String stockId;
    private List<StockPlateInfoMapping> plateInfos;
}
