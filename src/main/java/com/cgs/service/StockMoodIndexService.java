package com.cgs.service;

import com.cgs.dao.StockMoodIndexFetchDAO;
import com.cgs.entity.StockMoodIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
public class StockMoodIndexService {

    @Autowired
    private StockMoodIndexFetchDAO stockMoodIndexFetchDAO;

    public List<StockMoodIndex> batchQueryStockMoodIndex(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat targetSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<StockMoodIndex> list = stockMoodIndexFetchDAO.batchQueryStockMoodIndex();
        if (!CollectionUtils.isEmpty(list)){
            for (StockMoodIndex index : list){
                try {
                    index.setReleaseDate(targetSimpleDateFormat.format(simpleDateFormat.parse(index.getReleaseDate())));
                } catch (ParseException e) {
                    log.error("exception in batchQueryStockMoodIndex:{}",e);
                }
            }
        }
        return list;
    }
}
