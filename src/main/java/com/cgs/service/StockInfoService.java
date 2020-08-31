package com.cgs.service;

import com.cgs.dao.StockInfoDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.vo.PageHelperVO;
import com.cgs.vo.StockInfoVO;
import com.cgs.vo.StockValueStaticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockInfoService {

    @Autowired
    private StockItemDAO stockItemDAO;
    @Autowired
    private StockInfoDAO stockInfoDAO;

    public List<StockInfoVO> queryStockInfoByStockIdAndStockName(String stockId,String stockName){
        List<StockItem> list = new ArrayList<>();
        if (StringUtils.isEmpty(stockId) && StringUtils.isEmpty(stockName)){
            list = stockItemDAO.queryAllStockList();
        }else if (!StringUtils.isEmpty(stockName) && StringUtils.isEmpty(stockId)){
            list = stockItemDAO.queryStockItemsByStockName(stockName);
        }else if (!StringUtils.isEmpty(stockId)){
            list = stockItemDAO.queryStockItemsByStockId(stockId);
        }
        List<StockInfoVO> resultList = new ArrayList<>();
        list.stream().forEach(e->{
            StockInfoVO stockInfoVO = new StockInfoVO();
            stockInfoVO.setStockId(e.getStockId());
            stockInfoVO.setStockName(e.getName());
            resultList.add(stockInfoVO);
        });
        return resultList;
    }

    public StockValueStaticsVO queryStockItemValue(HttpServletRequest httpServletRequest){
        StockValueStaticsVO vo  = new StockValueStaticsVO();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        Integer tenValueCount =  stockInfoDAO.queryStockInfoCountByValue(0,10);
        Integer fiftyValueCount = stockInfoDAO.queryStockInfoCountByValue(10,50);
        Integer oneHundredCount  = stockInfoDAO.queryStockInfoCountByValue(50,100);
        Integer twoHundredCount = stockInfoDAO.queryStockInfoCountByValue(100,200);
        Integer fiveHundredCount = stockInfoDAO.queryStockInfoCountByValue(200,500);
        Integer oneThousandCount = stockInfoDAO.queryStockInfoCountByValue(500,1000);
        Integer twoThousandCount = stockInfoDAO.queryStockInfoCountByValue(1000,2000);
        Integer fiveThousandCount = stockInfoDAO.queryStockInfoCountByValue(2000,5000);
        Integer tenThousandCount = stockInfoDAO.queryStockInfoCountByValue(5000,10000);
        Integer largerCount = stockInfoDAO.queryStockInfoCountLargerThanValue(10000);
        Pair<Integer,Integer> tenPair = Pair.of(10,tenValueCount);
        Pair<Integer,Integer> fiftyValueCountPair = Pair.of(50,Optional.ofNullable(fiftyValueCount).orElseGet(()->0));
        Pair<Integer,Integer> oneHundredCountPair = Pair.of(100,Optional.ofNullable(oneHundredCount).orElseGet(()->0));
        Pair<Integer,Integer> twoHundredCountPair = Pair.of(200,Optional.ofNullable(twoHundredCount).orElseGet(()->0));
        Pair<Integer,Integer> fiveHundredCountPair = Pair.of(500,Optional.ofNullable(fiveHundredCount).orElseGet(()->0));
        Pair<Integer,Integer> oneThousandCountPair = Pair.of(1000,Optional.ofNullable(oneThousandCount).orElseGet(()->0));
        Pair<Integer,Integer> twoThousandCountPair = Pair.of(2000,Optional.ofNullable(twoThousandCount).orElseGet(()->0));
        Pair<Integer,Integer> fiveThousandCountPair = Pair.of(5000,Optional.ofNullable(fiveThousandCount).orElseGet(()->0));
        Pair<Integer,Integer> tenThousandCountPair = Pair.of(10000,Optional.ofNullable(tenThousandCount).orElseGet(()->0));
        Pair<Integer,Integer> largerCountPair = Pair.of(20000, Optional.ofNullable(largerCount).orElseGet(()->0));
        List<Pair<Integer,Integer>> resultPair = new ArrayList<>();
        resultPair.add(tenPair);
        resultPair.add(fiftyValueCountPair);
        resultPair.add(oneHundredCountPair);
        resultPair.add(twoHundredCountPair);
        resultPair.add(fiveHundredCountPair);
        resultPair.add(oneThousandCountPair);
        resultPair.add(twoThousandCountPair);
        resultPair.add(fiveThousandCountPair);
        resultPair.add(tenThousandCountPair);
        resultPair.add(largerCountPair);
        vo.setValuePairList(resultPair);
        vo.setDate(date);
        return vo;
    }

    public PageHelperVO queryStockMarketValueType(String type){
        PageHelperVO vo = new PageHelperVO();
        return vo;
    }
}
