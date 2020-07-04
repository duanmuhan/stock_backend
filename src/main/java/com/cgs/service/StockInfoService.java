package com.cgs.service;

import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.vo.StockInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockInfoService {

    @Autowired
    private StockItemDAO stockItemDAO;

    public List<StockInfoVO> queryStockInfoByStockIdAndStockName(String stockId,String stockName){
        List<StockItem> list = new ArrayList<>();
        if (StringUtils.isEmpty(stockId) && StringUtils.isEmpty(stockName)){
            list = stockItemDAO.queryAllStockList();
        }else if (!StringUtils.isEmpty(stockName) && StringUtils.isEmpty(stockId)){
            list = stockItemDAO.queryStockItemsByStockName(stockName);
        }else if (!StringUtils.isEmpty(stockId)){
            list = stockItemDAO.queryStockItemsByStockId(stockId);
        }
        List<StockInfoVO> resultList = new ArrayList<>();
        list.stream().forEach(e->{
            StockInfoVO stockInfoVO = new StockInfoVO();
            stockInfoVO.setStockId(e.getStockId());
            stockInfoVO.setStockName(e.getName());
            resultList.add(stockInfoVO);
        });
        return resultList;
    }
}
