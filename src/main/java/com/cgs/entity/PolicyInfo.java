package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PolicyInfo implements Serializable {
    private String title;
    private String targetPlate;
    private String targetPlateId;
    private String source;
    private String release_date;
    private String platform;
}
