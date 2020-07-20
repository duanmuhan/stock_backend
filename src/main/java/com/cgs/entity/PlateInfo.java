package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlateInfo implements Serializable {
    private String plateId;
    private String plateName;
    private Integer type;
    private String date;
}
