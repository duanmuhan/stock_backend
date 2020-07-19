package com.cgs.vo;

import com.cgs.entity.AverageItem;
import lombok.Data;

import java.util.List;

@Data
public class AverageVO {
    private String stockId;
    private List<AverageItem> fiveDayList;
    private List<AverageItem> tenDayList;
}
