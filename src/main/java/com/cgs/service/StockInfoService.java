package com.cgs.service;

import com.cgs.dao.StockInfoDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.vo.StockInfoVO;
import com.cgs.vo.StockValueStaticsVO;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

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
        Integer tenValueCount =  stockInfoDAO.queryStockInfoCountByValue(0,10,date);
        Integer fiftyValueCount = stockInfoDAO.queryStockInfoCountByValue(10,50,date);
        Integer oneHundredCount  = stockInfoDAO.queryStockInfoCountByValue(50,100,date);
        Integer twoHundredCount = stockInfoDAO.queryStockInfoCountByValue(100,200,date);
        Integer fiveHundredCount = stockInfoDAO.queryStockInfoCountByValue(200,500,date);
        Integer oneThousandCount = stockInfoDAO.queryStockInfoCountByValue(500,1000,date);
        Integer twoThousandCount = stockInfoDAO.queryStockInfoCountByValue(1000,2000,date);
        Integer fiveThousandCount = stockInfoDAO.queryStockInfoCountByValue(2000,5000,date);
        Integer tenThousandCount = stockInfoDAO.queryStockInfoCountByValue(5000,10000,date);
        Integer largerCount = stockInfoDAO.queryStockInfoCountLargerThanValue(10000,date);
        Pair<Integer,Integer> tenPair = new Pair<>(10,tenValueCount);
        Pair<Integer,Integer> fiftyValueCountPair = new Pair<>(50,fiftyValueCount);
        Pair<Integer,Integer> oneHundredCountPair = new Pair<>(100,oneHundredCount);
        Pair<Integer,Integer> twoHundredCountPair = new Pair<>(200,twoHundredCount);
        Pair<Integer,Integer> fiveHundredCountPair = new Pair<>(500,fiveHundredCount);
        Pair<Integer,Integer> oneThousandCountPair = new Pair<>(1000,oneThousandCount);
        Pair<Integer,Integer> twoThousandCountPair = new Pair<>(2000,twoThousandCount);
        Pair<Integer,Integer> fiveThousandCountPair = new Pair<>(5000,fiveThousandCount);
        Pair<Integer,Integer> tenThousandCountPair = new Pair<>(10000,tenThousandCount);
        Pair<Integer,Integer> largerCountPair = new Pair<>(20000,largerCount);
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
}
