package com.cgs.vo;

import com.cgs.entity.AverageItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AverageVO implements Serializable {
    private String stockId;
    private List<AverageItem> fiveDayList;
    private List<AverageItem> tenDayList;
    private List<AverageItem> twentiesDayList;
    private List<AverageItem> sixtiesDayList;
    private List<AverageItem> oneHundredAndTwentiesDayList;
}
