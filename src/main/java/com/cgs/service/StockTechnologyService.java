package com.cgs.service;

import com.cgs.dao.StockTechnologyDAO;
import com.cgs.dao.StockTechnologyScoreDAO;
import com.cgs.entity.StockTechnology;
import com.cgs.entity.StockTechnologyScore;
import com.cgs.vo.forms.StockTechnologyScoreVO;
import com.cgs.vo.forms.StockTechnologyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockTechnologyService {

    @Autowired
    private StockTechnologyDAO stockTechnologyDAO;
    @Autowired
    private StockTechnologyScoreDAO stockTechnologyScoreDAO;

    @Cacheable(value = "stockInfo::queryLatestStockTechnologyScoreByPage",key = "#pageNo + '-' + #pageSize")
    public List<StockTechnologyScoreVO> queryLatestStockTechnologyScoreByPage(Integer pageSize, Integer pageNo){
        List<StockTechnologyScoreVO> list = new ArrayList<>();
        List<StockTechnologyScore> stockTechnologyScoreList = stockTechnologyScoreDAO.queryLatestStockTechnologyScoreList();
        if (CollectionUtils.isEmpty(stockTechnologyScoreList)){
            return list;
        }
        stockTechnologyScoreList = stockTechnologyScoreList.stream().sorted(Comparator.comparing(StockTechnologyScore::getScore).reversed()).collect(Collectors.toList()); ;
        int startIndex = pageNo * pageSize;
        int endIndex = (pageNo+1) * pageSize -1;
        if (endIndex > stockTechnologyScoreList.size()-1){
            endIndex = stockTechnologyScoreList.size() - 1;
        }
        List<StockTechnologyScore> stockTechnologyScores = stockTechnologyScoreList.subList(startIndex,endIndex);
        list = stockTechnologyScores.stream().map(e->{
            StockTechnologyScoreVO vo = new StockTechnologyScoreVO();
            vo.setStockId(e.getStockId());
            vo.setStockName(e.getStockName());
            vo.setScore(e.getScore());
            vo.setReleaseDate(e.getReleaseDate());
            return vo;
        }).collect(Collectors.toList());
        return list;
    }

    public StockTechnologyScoreVO queryStockTechnologyScoreByStockId(String stockId){
        StockTechnologyScoreVO vo = new StockTechnologyScoreVO();
        StockTechnologyScore stockTechnologyScore = stockTechnologyScoreDAO.queryStockTechnologyScoreByStockId(stockId);
        if (ObjectUtils.isEmpty(stockTechnologyScore)){
            return vo;
        }
        vo.setStockId(stockTechnologyScore.getStockId());
        vo.setStockName(stockTechnologyScore.getStockName());
        vo.setScore(stockTechnologyScore.getScore());
        vo.setReleaseDate(stockTechnologyScore.getReleaseDate());
        return vo;
    }

    public List<StockTechnologyVO> queryStockTechnologyList(Integer pageNo, Integer pageSize){
        List<StockTechnology> stockTechnologyList = stockTechnologyDAO.queryLatestStockTechnologyList();
        if (CollectionUtils.isEmpty(stockTechnologyList)){
            return null;
        }
        int startIndex = pageNo * pageSize;
        int endIndex = (pageNo+1) * pageSize -1;
        if (endIndex > stockTechnologyList.size()-1){
            endIndex = stockTechnologyList.size() - 1;
        }
        List<StockTechnology> technologyList = stockTechnologyList.subList(startIndex,endIndex);
        List<StockTechnologyVO> list = technologyList.stream().map(e->{
            StockTechnologyVO vo = new StockTechnologyVO();
            vo.setStockId(e.getStockId());
            vo.setDescStr(e.getDescStr());
            vo.setQueryStr(e.getQueryStr());
            vo.setSpecial(e.getSpecial());
            vo.setTag(e.getTag());
            vo.setType(e.getType());
            vo.setReleaseDate(e.getReleaseDate());
            return vo;
        }).collect(Collectors.toList());
        return list;
    }

    public StockTechnologyVO queryStockTechnologyByStockId(String stockId){
        StockTechnologyVO vo = new StockTechnologyVO();
        StockTechnology stockTechnology = stockTechnologyDAO.queryStockTechnologyByStockId(stockId);
        if (ObjectUtils.isEmpty(stockTechnology)){
            vo.setStockId(stockTechnology.getStockId());
            vo.setReleaseDate(stockTechnology.getReleaseDate());
            vo.setType(stockTechnology.getType());
            vo.setTag(stockTechnology.getTag());
            vo.setSpecial(stockTechnology.getSpecial());
            vo.setQueryStr(stockTechnology.getQueryStr());
            vo.setDescStr(stockTechnology.getDescStr());
        }
        return vo;
    }
}
