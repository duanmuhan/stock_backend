package com.cgs.service;

import com.cgs.dao.AverageDAO;
import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockPlateInfoMappingDAO;
import com.cgs.entity.*;
import com.cgs.vo.AverageVO;
import com.cgs.vo.KItemVO;
import com.cgs.vo.StockBasicVO;
import com.cgs.vo.StockPlateVO;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public List<StockBasicVO> queryStockBasicInfo(){
        List<StockBasicVO> list = new ArrayList<>();
        List<FinanceInfo> financeInfos = financeInfoDAO.queryFinanceInfo();
        if (CollectionUtils.isEmpty(financeInfos)){
            return list;
        }
        Map<String,List<FinanceInfo>> financeMap = financeInfos.stream().collect(Collectors.groupingBy(e->e.getStockId()));
        List<FinanceInfo> financeInfoList = new ArrayList<>();
        for (String key : financeMap.keySet()){
            if (CollectionUtils.isEmpty(financeMap.get(key))){
                continue;
            }
            FinanceInfo financeInfo = financeMap.get(key).stream().sorted(Comparator.comparing(FinanceInfo::getReleaseDate).reversed()).limit(1).findFirst().get();
            financeInfoList.add(financeInfo);
        }
        financeInfoList = financeInfoList.stream().sorted(Comparator.comparing(FinanceInfo::getBasicEarningsPerCommonShare)).collect(Collectors.toList());
        List<String> stockIdList = financeInfoList.stream().map(e->e.getStockId()).collect(Collectors.toList());
        return list;
    }
}
