package com.cgs.service;

import com.cgs.dao.StockMoodIndexFetchDAO;
import com.cgs.entity.StockMoodIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMoodIndexService {

    @Autowired
    private StockMoodIndexFetchDAO stockMoodIndexFetchDAO;

    public List<StockMoodIndex> batchQueryStockMoodIndex(){
        List<StockMoodIndex> list = stockMoodIndexFetchDAO.batchQueryStockMoodIndex();
        return list;
    }
}
