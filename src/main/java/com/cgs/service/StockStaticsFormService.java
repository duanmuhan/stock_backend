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
import com.cgs.vo.StockEarningPriceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
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

    @Cacheable(value = "stock::earningPerPrice",key = "#pageNo" + "#pageSize" + "#date")
    public StockEarningPriceVO queryStockEarningPerPrice(String date, Integer pageNo, Integer pageSize){

        StockEarningPriceVO stockEarningPriceVO = new StockEarningPriceVO();

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
                    sb.append(e.getPlateName()).append(";");
                });
                if (ObjectUtils.isEmpty(kItemMap.get(financeInfo.getStockId()))){
                    continue;
                }
                DecimalFormat df = new DecimalFormat("#.00");
                vo.setEarningsPerPrice("--".equals(financeInfo.getUDPPS()) ? 0 : Double.valueOf(df.format(Double.valueOf(financeInfo.getUDPPS())/kItemMap.get(financeInfo.getStockId()).getClosePrice())));
                vo.setPrice(kItemMap.get(financeInfo.getStockId()).getClosePrice());
                vo.setPlate(sb.toString());
                vo.setDate(kItemMap.get(financeInfo.getStockId()).getDate());
                vo.setStockName(stringStockItemMap.get(financeInfo.getStockId()).getName());
                list.add(vo);
            }
        }
        List<StockEarningPerPriceVO> finalResult = list.stream().sorted(Comparator.comparing(StockEarningPerPriceVO::getEarningsPerPrice).reversed()).collect(Collectors.toList());

        List<StockEarningPerPriceVO> resultList = new ArrayList<>();
        if ((pageNo) * pageSize > finalResult.size()){
            resultList = finalResult.subList((pageNo-1)*pageSize,list.size()-1);
        }
        if ((pageNo) * pageSize < list.size()){
            resultList = finalResult.subList((pageNo-1)*pageSize,(pageNo)*pageSize);
        }
        stockEarningPriceVO.setDate(date);
        stockEarningPriceVO.setList(resultList);
        stockEarningPriceVO.setPageNo(pageNo);
        stockEarningPriceVO.setPageSize(pageSize);
        stockEarningPriceVO.setSize(finalResult.size());
        return stockEarningPriceVO;
    }
}
