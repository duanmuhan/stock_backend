package com.cgs.esdao.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author caoguangshu
 * @date 2021/2/12
 * @time 9:09 下午
 */
@Data
public class BaseEsPo {

    /** 文档id **/
    @JSONField(serialize = false)
    private String id;

    /** 排序分值 **/
    @JSONField(serialize = false)
    protected BigDecimal sortValue;
}
