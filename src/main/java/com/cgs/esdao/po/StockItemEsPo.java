package com.cgs.esdao.po;

import lombok.Data;

/**
 * @author caoguangshu
 * @date 2021/2/12
 * @time 9:45 下午
 */
@Data
public class StockItemEsPo extends BaseEsPo {

    private String stockId;
    private String exchangeId;
    private String name;
    private String listingDate;
    private Long updateTime;
}
