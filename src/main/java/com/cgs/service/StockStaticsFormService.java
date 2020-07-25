package com.cgs.service;

import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockPlateInfoMappingDAO;
import com.cgs.entity.FinanceInfo;
import com.cgs.entity.KItem;
import com.cgs.entity.StockItem;
import com.cgs.entity.StockPlateInfoMapping;
import com.cgs.vo.StockEarningPerPriceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StockStaticsFormService {

    @Autowired
    private FinanceInfoDAO financeInfoDAO;
    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;
    @Autowired
    private StockPlateInfoMappingDAO stockPlateInfoMappingDAO;

    public List<StockEarningPerPriceVO> queryStockEarningPerPrice(String date){

        List<FinanceInfo> financeInfos = financeInfoDAO.queryFinanceInfo();
        List<KItem> kItemList = kItemDAO.queryLatestValue();
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(financeInfos) || CollectionUtils.isEmpty(kItemList) || CollectionUtils.isEmpty(stockItemList)){
            return null;
        }
        Map<String,StockItem> stringStockItemMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId,Function.identity()));
        Map<String,KItem> kItemMap = kItemList.stream().collect(Collectors.toMap(KItem::getStockId, Function.identity()));
        Map<String,List<FinanceInfo>> financeMap = financeInfos.parallelStream().collect(Collectors.groupingBy(e->e.getStockId()));
        List<FinanceInfo> financeInfoList = new ArrayList<>();
        for (String key : financeMap.keySet()){
            if (CollectionUtils.isEmpty(financeMap.get(key))){
                continue;
            }
            FinanceInfo financeInfo = financeMap.get(key).parallelStream().sorted(Comparator.comparing(FinanceInfo::getReleaseDate).reversed()).limit(1).findFirst().get();
            financeInfoList.add(financeInfo);
        }
        List<StockEarningPerPriceVO> list = new ArrayList<>();
        for (FinanceInfo financeInfo : financeInfoList) {
            StockEarningPerPriceVO vo = new StockEarningPerPriceVO();
            vo.setStockId(financeInfo.getStockId());
            vo.setDate(kItemList.get(0).getDate());
            List<StockPlateInfoMapping> stockPlateInfoMappings = stockPlateInfoMappingDAO.queryPlateInfoByStockId(financeInfo.getStockId());
            if (!CollectionUtils.isEmpty(stockPlateInfoMappings)){
                StringBuilder sb = new StringBuilder();
                stockPlateInfoMappings.forEach(e->{
                    sb.append(e.getPlateName());
                });
                vo.setEarningsPerPrice("--".equals(financeInfo.getBasicEarningsPerCommonShare()) ? 0 : Double.valueOf(financeInfo.getBasicEarningsPerCommonShare())/kItemMap.get(financeInfo.getStockId()).getClosePrice());
                vo.setPrice(kItemMap.get(financeInfo.getStockId()).getClosePrice());
                vo.setPlate(sb.toString());
                vo.setDate(kItemMap.get(financeInfo.getStockId()).getDate());
                vo.setStockName(stringStockItemMap.get(financeInfo.getStockId()).getName());
            }
        }
        return list;
    }
}
