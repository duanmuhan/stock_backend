package com.cgs.service;

import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockTechnologyDAO;
import com.cgs.dao.StockTechnologyScoreDAO;
import com.cgs.entity.StockItem;
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

    @Cacheable(value = "stockInfo::queryLatestStockTechnologyScoreByPage",key = "#pageNo + '-' + #pageSize")
    public List<StockTechnologyScoreVO> queryLatestStockTechnologyScoreByPage(Integer pageSize, Integer pageNo){
        List<StockTechnologyScoreVO> list = new ArrayList<>();
        List<StockTechnologyScore> stockTechnologyScoreList = stockTechnologyScoreDAO.queryLatestStockTechnologyScoreList();
        if (CollectionUtils.isEmpty(stockTechnologyScoreList)){
            return list;
        }
        stockTechnologyScoreList = stockTechnologyScoreList.stream().sorted(Comparator.comparing(StockTechnologyScore::getScore).reversed()).collect(Collectors.toList());
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
        List<StockTechnologyVO> resultList = new ArrayList<>();
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        Map<String,StockItem> stockItemMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId, Function.identity()));
        Map<String,List<StockTechnology>> stockTechnologyMap = stockTechnologyList.stream().collect(Collectors.groupingBy(StockTechnology::getStockId));
        for (Map.Entry<String,List<StockTechnology>> entry : stockTechnologyMap.entrySet()){
            StockTechnologyVO vo = new StockTechnologyVO();
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
            vo.setStockId(entry.getKey());
            vo.setStockName(stockItemMap.get(entry.getKey()).getName());
            if (!CollectionUtils.isEmpty(buyList)){
                vo.setBuy(buyList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(",")));
                vo.setBuyCount(buyList.size());
            }
            if (!CollectionUtils.isEmpty(sellList)){
                vo.setSell(sellList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(",")));
                vo.setBuyCount(sellList.size());
            }
            if (!CollectionUtils.isEmpty(eventList)){
                vo.setEvent(eventList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(",")));
            }
            vo.setReleaseDate(list.get(0).getReleaseDate());
            resultList.add(vo);
        }
        int startIndex = pageNo * pageSize;
        int endIndex = (pageNo+1) * pageSize -1;
        if (endIndex > resultList.size()-1){
            endIndex = resultList.size() - 1;
        }
        resultList = resultList.stream().sorted(Comparator.comparing(StockTechnologyVO::getBuyCount).reversed()).collect(Collectors.toList());
        return resultList.subList(startIndex,endIndex);
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
