package com.cgs.service;

import com.cgs.dao.NewsDAO;
import com.cgs.entity.PolicyInfo;
import com.cgs.vo.StockInfoVO;
import com.cgs.vo.news.StockNewsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            vo.setReleaseDate(e.getRelease_date());
            stockNewsVOS.add(vo);
        });

        List<StockNewsVO> resultList = stockNewsVOS.stream().filter(e->{
            return !StringUtils.isEmpty(e.getTargetPlate()) && !StringUtils.isEmpty(e.getTargetPlateId());
        }).collect(Collectors.toList());
       return resultList;
    }
}
