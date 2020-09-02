package com.cgs.service;

import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockTechnologyDAO;
import com.cgs.dao.StockTechnologyScoreDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.StockTechnology;
import com.cgs.entity.StockTechnologyScore;
import com.cgs.vo.PageHelperVO;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StockTechnologyService {

    @Autowired
    private StockTechnologyDAO stockTechnologyDAO;
    @Autowired
    private StockTechnologyScoreDAO stockTechnologyScoreDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

//    @Cacheable(value = "stockInfo::queryLatestStockTechnologyScoreByPage",key = "#pageNo + '-' + #pageSize")
    public PageHelperVO queryLatestStockTechnologyScoreByPage(Integer pageSize, Integer pageNo){
        PageHelperVO vo = new PageHelperVO();
        List<StockTechnologyScoreVO> list = new ArrayList<>();
        List<StockTechnologyScore> stockTechnologyScoreList = stockTechnologyScoreDAO.queryLatestStockTechnologyScoreList();
        if (CollectionUtils.isEmpty(stockTechnologyScoreList)){
            return vo;
        }
        List<StockTechnologyScore> stockTechnologyScores = new ArrayList<>();
        stockTechnologyScoreList = stockTechnologyScoreList.stream().sorted(Comparator.comparing(StockTechnologyScore::getScore).reversed()).collect(Collectors.toList());
        if (pageNo * pageSize > stockTechnologyScoreList.size()){
            return vo;
        }
        if ((pageNo + 1) * pageSize > stockTechnologyScoreList.size()){
            stockTechnologyScores = new ArrayList<>(stockTechnologyScoreList.subList((pageNo)*pageSize,stockTechnologyScoreList.size()-1));
        }
        if ((pageNo + 1) * pageSize < stockTechnologyScoreList.size()){
            stockTechnologyScores = new ArrayList<>(stockTechnologyScoreList.subList((pageNo)*pageSize,(pageNo+1)*pageSize));
        }
        list = stockTechnologyScores.stream().map(e->{
            StockTechnologyScoreVO stockTechnologyScoreVO = new StockTechnologyScoreVO();
            stockTechnologyScoreVO.setStockId(e.getStockId());
            stockTechnologyScoreVO.setStockName(e.getStockName());
            stockTechnologyScoreVO.setScore(e.getScore());
            stockTechnologyScoreVO.setReleaseDate(e.getReleaseDate());
            return stockTechnologyScoreVO;
        }).collect(Collectors.toList());
        vo.setTotal(stockTechnologyScoreList.size());
        vo.setRows(list);
        return vo;
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

    @Cacheable(value = "stockInfo::queryStockTechnologyList",key = "#pageNo + '-' + #pageSize")
    public PageHelperVO queryStockTechnologyList(Integer pageNo, Integer pageSize){
        PageHelperVO vo = new PageHelperVO();
        List<StockTechnology> stockTechnologyList = stockTechnologyDAO.queryLatestStockTechnologyList();
        if (CollectionUtils.isEmpty(stockTechnologyList)){
            return null;
        }
        List<StockTechnologyVO> resultList = new ArrayList<>();
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        Map<String,StockItem> stockItemMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId, Function.identity()));
        Map<String,List<StockTechnology>> stockTechnologyMap = stockTechnologyList.stream().collect(Collectors.groupingBy(StockTechnology::getStockId));
        for (Map.Entry<String,List<StockTechnology>> entry : stockTechnologyMap.entrySet()){
            StockTechnologyVO stockTechnologyVO = new StockTechnologyVO();
            List<StockTechnology> list = entry.getValue();
            if (CollectionUtils.isEmpty(list)){
                continue;
            }
            List<StockTechnology> sellList = list.stream().filter(e->{
                return "sell".equals(e.getType());
            }).collect(Collectors.toList());
            List<StockTechnology> buyList = list.stream().filter(e->{
                return "buy".equals(e.getType());
            }).collect(Collectors.toList());
            List<StockTechnology> eventList = list.stream().filter(e->{
                return "event".equals(e.getType());
            }).collect(Collectors.toList());
            stockTechnologyVO.setStockId(entry.getKey());
            if (!stockItemMap.containsKey(entry.getKey())){
                continue;
            }
            stockTechnologyVO.setStockName(stockItemMap.get(entry.getKey()).getName());
            if (!CollectionUtils.isEmpty(buyList)){
                stockTechnologyVO.setBuy(buyList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(",<br>")));
                stockTechnologyVO.setBuyCount(buyList.size());
            }
            if (!CollectionUtils.isEmpty(sellList)){
                stockTechnologyVO.setSell(sellList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(",<br>")));
                stockTechnologyVO.setSellCount(sellList.size());
            }
            if (!CollectionUtils.isEmpty(eventList)){
                stockTechnologyVO.setEvent(eventList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(",<br>")));
            }
            stockTechnologyVO.setReleaseDate(list.get(0).getReleaseDate());
            resultList.add(stockTechnologyVO);
        }
        resultList = resultList.stream().sorted(Comparator.comparing(StockTechnologyVO::getBuyCount).reversed()).collect(Collectors.toList());
        vo.setTotal(resultList.size());
        if (pageNo * pageSize > resultList.size()){
            return vo;
        }
        if ((pageNo + 1) * pageSize > resultList.size()){
            vo.setRows(new ArrayList<>(resultList.subList((pageNo)*pageSize,resultList.size()-1)));
        }
        if ((pageNo + 1) * pageSize < resultList.size()){
            vo.setRows(new ArrayList<>(resultList.subList((pageNo)*pageSize,(pageNo+1)*pageSize)));
        }
        return vo;
    }

    public StockTechnologyVO queryStockTechnologyByStockId(String stockId){
        StockTechnologyVO vo = new StockTechnologyVO();
        StockTechnology stockTechnology = stockTechnologyDAO.queryStockTechnologyByStockId(stockId);
        if (ObjectUtils.isEmpty(stockTechnology)){
            vo.setStockId(stockTechnology.getStockId());
            vo.setReleaseDate(stockTechnology.getReleaseDate());
        }
        return vo;
    }
}
