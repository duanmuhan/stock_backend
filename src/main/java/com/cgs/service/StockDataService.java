package com.cgs.service;

import com.cgs.dao.*;
import com.cgs.entity.*;
import com.cgs.vo.*;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StockDataService {

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private AverageDAO averageDAO;
    @Autowired
    private StockPlateInfoMappingDAO stockPlateInfoMappingDAO;
    @Autowired
    private FinanceInfoDAO financeInfoDAO;

    public KItemVO queryKItemByStockId(String stockId){
        KItemVO vo = new KItemVO();
        List<KItem> items = kItemDAO.queryKItemsbyStockId(stockId);
        if (!CollectionUtils.isEmpty(items)){
            Collections.reverse(items);
        }
        vo.setStockId(stockId);
        vo.setKItemList(items);
        return vo;
    }

    public AverageVO queryAverageItemByStockId(String stockId){
        AverageVO vo = new AverageVO();
        List<AverageItem> list = averageDAO.queryAverageItemListByStockId(stockId);
        vo.setStockId(stockId);
        vo.setList(list);
        return vo;
    }

    public StockPlateVO queryStockPlateInfoByStockId(String stockId){
        StockPlateVO vo = new StockPlateVO();
        List<StockPlateInfoMapping> list = stockPlateInfoMappingDAO.queryPlateInfoByStockId(stockId);
        vo.setStockId(stockId);
        vo.setPlateInfos(list);
        return vo;
    }

    public StockOverViewVO queryTodayStockOverViewByStockId(String stockId){
        StockOverViewVO viewVO = new StockOverViewVO();
        List<KItem> kItemList = kItemDAO.queryLatestValueByStockId(stockId);
        if (ObjectUtils.isEmpty(kItemList)){
            return viewVO;
        }
        KItem todayKItem = kItemList.get(0);
        KItem yesterdayKItem = kItemList.get(1);
        viewVO.setPrice(todayKItem.getClosePrice());
        viewVO.setDealAmount(todayKItem.getDealAmount());
        viewVO.setStockId(stockId);
        viewVO.setDealCash(todayKItem.getDealCash());
        viewVO.setPriceRate(String.valueOf((todayKItem.getClosePrice()-todayKItem.getOpenPrice())/todayKItem.getOpenPrice() * 100));
        viewVO.setAmountRate(String.valueOf((todayKItem.getDealAmount() - yesterdayKItem.getDealAmount())/yesterdayKItem.getDealAmount() * 100));
        return viewVO;
    }
}
