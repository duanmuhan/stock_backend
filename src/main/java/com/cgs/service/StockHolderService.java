package com.cgs.service;

import com.cgs.dao.StockHolderDAO;
import com.cgs.entity.StockHolder;
import com.cgs.vo.StockHolderCoverRateVO;
import com.cgs.vo.StockHolderMarketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockHolderService {

    @Autowired
    private StockHolderDAO stockHolderDAO;

    public List<StockHolderMarketVO> queryStockHolderMarket(HttpServletRequest httpServletRequest){
        List<StockHolder> list =  stockHolderDAO.queryNewestStockHolder();

        return new ArrayList<>();
    }

    public List<StockHolderCoverRateVO> queryStockHolderOrder(HttpServletRequest httpServletRequest){
        List<StockHolder> list =  stockHolderDAO.queryNewestStockHolder();
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        list = list.stream().filter(e->{
            return !StringUtils.isEmpty(e.getTopTenStockFlowHolder()) && !e.getTopTenStockFlowHolder().equals("--");
        }).collect(Collectors.toList());
        List<StockHolderCoverRateVO> coverList = list.stream().map(e->{
            StockHolderCoverRateVO vo = new StockHolderCoverRateVO();
            vo.setStockId(e.getStockId());
            vo.setTopTenStockHolder(e.getTopTenStockHolder());
            return vo;
        }).collect(Collectors.toList());
        coverList = coverList.stream().sorted(Comparator.comparing(StockHolderCoverRateVO::getTopTenStockHolder).reversed()).collect(Collectors.toList());
        return coverList;
    }
}
