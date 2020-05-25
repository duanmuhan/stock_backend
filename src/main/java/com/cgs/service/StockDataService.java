package com.cgs.service;

import com.cgs.dao.KItemDAO;
import com.cgs.entity.KItem;
import com.cgs.vo.KItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockDataService {

    @Autowired
    private KItemDAO kItemDAO;

    public KItemVO queryKItemByStockId(String stockId){
        KItemVO vo = new KItemVO();
        List<KItem> items = kItemDAO.queryKItemsbyStockId(stockId);
        vo.setStockId(stockId);
        vo.setKItemList(items);
        return vo;
    }
}
