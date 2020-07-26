package com.cgs.vo;

import lombok.Data;

import java.util.List;

@Data
public class StockEarningPriceVO {
    private String date;
    private Integer pageNo;
    private Integer pageSize;
    private Integer size;
    private List<StockEarningPerPriceVO> list;
}
