package com.cgs.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageHelperVO implements Serializable {
    private Integer total;
    private Object rows;

    public PageHelperVO(){
        this.total = 0;
        this.rows = null;
    }
}
