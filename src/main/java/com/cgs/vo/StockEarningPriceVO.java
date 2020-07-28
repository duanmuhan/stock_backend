package com.cgs.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StockEarningPriceVO implements Serializable {
    private String date;
    private Integer pageNo;
    private Integer pageSize;
    private Integer size;
    private List<StockEarningPerPriceVO> list;
}
