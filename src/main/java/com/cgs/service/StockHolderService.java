package com.cgs.service;

import com.cgs.dao.StockHolderDAO;
import com.cgs.entity.StockHolder;
import com.cgs.vo.StockHolderMarketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockHolderService {

    @Autowired
    private StockHolderDAO stockHolderDAO;

    public List<StockHolderMarketVO> queryStockHolderMarket(HttpServletRequest httpServletRequest){
        List<StockHolder> list =  stockHolderDAO.queryNewestStockHolder();
        return new ArrayList<>();
    }
}
