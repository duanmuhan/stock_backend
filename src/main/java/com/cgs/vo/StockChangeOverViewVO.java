package com.cgs.vo;

import lombok.Data;

import java.util.List;

@Data
public class StockChangeOverViewVO {
    private List<StockChangeVO> list;
    private String date;
}
