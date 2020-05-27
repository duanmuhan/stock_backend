package com.cgs.service;

import com.cgs.dao.AverageDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.entity.AverageItem;
import com.cgs.entity.KItem;
import com.cgs.vo.AverageVO;
import com.cgs.vo.KItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockDataService {

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private AverageDAO averageDAO;

    public KItemVO queryKItemByStockId(String stockId){
        KItemVO vo = new KItemVO();
        List<KItem> items = kItemDAO.queryKItemsbyStockId(stockId);
        vo.setStockId(stockId);
        vo.setKItemList(items);
        return vo;
    }

    public AverageVO queryAverageItemByStockId(String stockId){
        AverageVO vo = new AverageVO();
        List<AverageItem> list = averageDAO.queryAverageItemListByStockId(stockId);
        vo.setStockId(stockId);
        vo.setList(list);
        return vo;
    }
}
