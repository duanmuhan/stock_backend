package com.cgs.vo;

import lombok.Data;

@Data
public class PageHelperVO {
    private Integer total;
    private Object rows;

    public PageHelperVO(){
        this.total = 0;
        this.rows = null;
    }
}
