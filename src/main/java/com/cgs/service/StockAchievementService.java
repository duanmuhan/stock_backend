package com.cgs.service;

import com.cgs.dao.StockAchievementDAO;
import com.cgs.vo.forms.StockAchievementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockAchievementService {
    @Autowired
    private StockAchievementDAO stockAchievementDAO;

    public List<StockAchievementVO> queryStockAchievementByPage(int pageNo,int pageSize){

        List<StockAchievementVO> list = new ArrayList<>();
        return list;
    }


}
