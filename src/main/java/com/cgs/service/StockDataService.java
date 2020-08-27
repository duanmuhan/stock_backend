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

import java.text.DecimalFormat;
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

    public KItemVO queryKItemByStockId(String stockId,Integer type){
        KItemVO vo = new KItemVO();
        List<KItem> items = kItemDAO.queryKItemsbyStockId(stockId,type);
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
        List<KItem> kItems = kItemDAO.queryLatestValueByStockId(stockId);
        if (CollectionUtils.isEmpty(kItems)){
            return viewVO;
        }
        StockInfo stockInfo = stockInfoDAO.queryLatestStockInfoByStockId(stockId);
        if (ObjectUtils.isEmpty(stockInfo)){
            return viewVO;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        StockItem item = stockItemDAO.queryStockItemByStockId(stockId);
        viewVO.setPrice(kItems.get(0).getClosePrice());
        viewVO.setDealAmount(kItems.get(0).getDealAmount());
        viewVO.setStockId(stockId);
        viewVO.setDealCash(stockInfo.getTotalTransactionAmount());
        viewVO.setStockName(item.getName());
        viewVO.setDealCash(stockInfo.getTotalTransactionAmount());
        viewVO.setPriceRate(df.format((kItems.get(0).getClosePrice()-kItems.get(1).getClosePrice())/kItems.get(0).getOpenPrice() * 100));
        viewVO.setChange(df.format(kItems.get(0).getClosePrice()-kItems.get(1).getClosePrice()) + "(" + viewVO.getPriceRate() + "%" + ")");
        viewVO.setAmountRate(String.valueOf((kItems.get(0).getDealAmount() - kItems.get(0).getDealAmount())/kItems.get(0).getDealAmount() * 100));
        viewVO.setDate(kItems.get(0).getDate());
        viewVO.setAverageTurnoverRate(stockInfo.getAverageTurnoverRate());
        return viewVO;
    }
}
