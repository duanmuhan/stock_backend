package com.cgs.vo;

import com.cgs.entity.KItem;
import lombok.Data;

import java.util.List;

@Data
public class KItemVO {

    private String stockId;
    List<KItem> kItemList;
}
