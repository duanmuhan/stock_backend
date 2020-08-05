package com.cgs.service;

import com.cgs.dao.StockAchievementDAO;
import com.cgs.entity.StockAchievement;
import com.cgs.vo.forms.StockAchievementVO;
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

    public List<StockAchievementVO> queryStockAchievementByPage(int pageNo,int pageSize){
        String date = "2020-04-30";
        List<StockAchievementVO> list = new ArrayList<>();
        Integer startIndex = pageNo * pageSize;
        Integer endIndex = (pageNo+1) * pageSize;
        List<StockAchievement> stockAchievements = stockAchievementDAO.queryStockAchievementOrderByProfileChangeRate(date,startIndex,endIndex);
        if (CollectionUtils.isEmpty(stockAchievements)){
            return null;
        }
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

    public Map<String,Long> queryStockAchievementGroup(){
        String date = "2020-04-30";
        List<StockAchievement> list = stockAchievementDAO.queryStockAchievement(date);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        Map<String,Long> resultMap = list.stream().collect(Collectors.groupingBy(StockAchievement::getAchievementType,Collectors.counting()));
        return resultMap;
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
