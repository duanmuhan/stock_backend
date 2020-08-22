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
    private StockItemDAO stockItemDAO;
    @Autowired
    private StockInfoDAO stockInfoDAO;

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
        vo.setFiveDayList(list);
        return vo;
    }

    public StockPlateVO queryStockPlateInfoByStockId(String stockId){
        StockPlateVO vo = new StockPlateVO();
        List<StockPlateInfoMapping> list = stockPlateInfoMappingDAO.queryPlateInfoMappingByStockId(stockId);
        vo.setStockId(stockId);
        vo.setPlateInfos(list);
        return vo;
    }

    public StockOverViewVO queryTodayStockOverViewByStockId(String stockId){
        StockOverViewVO viewVO = new StockOverViewVO();
        KItem kItem = kItemDAO.queryLatestValueByStockId(stockId);
        if (ObjectUtils.isEmpty(kItem)){
            return viewVO;
        }
        StockInfo stockInfo = stockInfoDAO.queryLatestStockInfoByStockId(stockId);
        if (ObjectUtils.isEmpty(stockInfo)){
            return viewVO;
        }
        StockItem item = stockItemDAO.queryStockItemByStockId(stockId);
        viewVO.setPrice(kItem.getClosePrice());
        viewVO.setDealAmount(kItem.getDealAmount());
        viewVO.setStockId(stockId);
        viewVO.setDealCash(stockInfo.getTotalTransactionAmount());
        viewVO.setStockName(item.getName());
        viewVO.setDealCash(kItem.getDealCash());
        viewVO.setPriceRate(String.valueOf((kItem.getClosePrice()-kItem.getOpenPrice())/kItem.getOpenPrice() * 100));
        viewVO.setAmountRate(String.valueOf((kItem.getDealAmount() - kItem.getDealAmount())/kItem.getDealAmount() * 100));
        return viewVO;
    }
}
