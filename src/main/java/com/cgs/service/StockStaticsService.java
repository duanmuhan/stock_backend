package com.cgs.service;

import com.cgs.entity.FinanceInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockStaticsService {

    public List<FinanceInfo> queryCurrentFinanceInfo(String stockId){
        return new ArrayList<>();
    }

    public List<String> queryBestFinanceInfoCompany(Integer limit, Integer season){
        List<String> result = new ArrayList<>();

        return result;
    }
}
