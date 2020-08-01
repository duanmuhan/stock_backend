package com.cgs.service;

import com.cgs.dao.NewsDAO;
import com.cgs.entity.PolicyInfo;
import com.cgs.vo.StockInfoVO;
import com.cgs.vo.news.StockNewsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<StockNewsVO> queryStockInfoByDate(String releaseDate){
       List<PolicyInfo> stockInfo =  newsDAO.queryNewsListBeforeDate(releaseDate);
       if (CollectionUtils.isEmpty(stockInfo)){
           return null;
       }
       List<StockNewsVO> stockNewsVOS = new ArrayList<>();
        stockInfo.stream().forEach(e->{
            StockNewsVO vo = new StockNewsVO();
            vo.setTitle(e.getTitle());
            vo.setPlatform(e.getPlatform());
            vo.setSource(e.getSource());
            vo.setTargetPlateId(e.getTargetPlateId());
            vo.setTargetPlate(e.getTargetPlate());
            stockNewsVOS.add(vo);
        });
       return stockNewsVOS;
    }
}
