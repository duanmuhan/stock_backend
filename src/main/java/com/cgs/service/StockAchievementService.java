package com.cgs.service;

import com.cgs.dao.StockAchievementDAO;
import com.cgs.entity.StockAchievement;
import com.cgs.vo.PageHelperVO;
import com.cgs.vo.StockAchievementGroupVO;
import com.cgs.vo.forms.StockAchievementVO;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockAchievementService {
    @Autowired
    private StockAchievementDAO stockAchievementDAO;

    public PageHelperVO queryStockAchievementByPage(int pageNo,int pageSize){
        PageHelperVO vo = new PageHelperVO();
        String date = "2020-04-30";
        List<StockAchievementVO> list = new ArrayList<>();
        Integer startIndex = pageNo * pageSize;
        Integer endIndex = (pageNo+1) * pageSize;
        Integer totalCount = stockAchievementDAO.queryStockAchievementCount(date);
        List<StockAchievement> stockAchievements = stockAchievementDAO.queryStockAchievementOrderByProfileChangeRate(date,startIndex,endIndex);
        if (CollectionUtils.isEmpty(stockAchievements)){
            return vo;
        }
        stockAchievements.stream().forEach(e->{
            StockAchievementVO stockAchievementVO = new StockAchievementVO();
            stockAchievementVO.setStockId(e.getStockId());
            stockAchievementVO.setStockName(e.getStockName());
            stockAchievementVO.setAchievementType(e.getAchievementType());
            stockAchievementVO.setAchievementTitle(e.getAchievementTitle());
            stockAchievementVO.setProfileChangeRate(e.getProfileChangeRate());
            stockAchievementVO.setProfileLastYear(e.getProfileLastYear());
            stockAchievementVO.setReleaseDate(e.getReleaseDate());
            list.add(stockAchievementVO);
        });
        vo.setTotal(totalCount);
        vo.setRows(stockAchievements);
        return vo;
    }

    public StockAchievementGroupVO queryStockAchievementGroup(){
        String date = "2020-04-30";
        List<StockAchievement> list = stockAchievementDAO.queryStockAchievement(date);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        Map<String,Long> resultMap = list.stream().collect(Collectors.groupingBy(StockAchievement::getAchievementType,Collectors.counting()));
        StockAchievementGroupVO vo = new StockAchievementGroupVO();
        vo.setDate(date);
        List<Pair<String,Long>> pairs = new ArrayList<>();
        for (Map.Entry<String,Long> entry : resultMap.entrySet()){
            Pair<String,Long> pair = new Pair<>(entry.getKey(),entry.getValue());
            pairs.add(pair);
        }
        vo.setList(pairs);
        return vo;
    }

    public List<StockAchievementVO> queryStockAchievementListByType(String type,Integer pageSize, Integer pageNo){
        String date = "2020-04-30";
        Integer startIndex = pageNo * pageSize;
        Integer endIndex = (pageNo+1) * pageSize;
        List<StockAchievement> stockAchievements = stockAchievementDAO.queryStockAchievementByType(date,type,startIndex,endIndex);
        if (CollectionUtils.isEmpty(stockAchievements)){
            return null;
        }
        List<StockAchievementVO> list = new ArrayList<>();
        stockAchievements.stream().forEach(e->{
            StockAchievementVO vo = new StockAchievementVO();
            vo.setStockId(e.getStockId());
            vo.setStockName(e.getStockName());
            vo.setAchievementType(e.getAchievementType());
            vo.setAchievementTitle(e.getAchievementTitle());
            vo.setProfileChangeRate(e.getProfileChangeRate());
            vo.setProfileLastYear(e.getProfileLastYear());
            vo.setReleaseDate(e.getReleaseDate());
            list.add(vo);
        });
        return list;
    }
}
