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
import com.cgs.vo.forms.StockChangeRateVO;
import com.cgs.vo.forms.StockPeriodChangeRateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    @Cacheable(value = "stockInfo::queryStockEarningPerPrice",key = "#pageNo + '-' + #pageSize")
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
            List<StockPlateInfoMapping> stockPlateInfoMappings = stockPlateInfoMappingDAO.queryPlateInfoMappingByStockId(financeInfo.getStockId());
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
            resultList = new ArrayList<>(finalResult.subList((pageNo-1)*pageSize,list.size()-1));
        }
        if ((pageNo) * pageSize < list.size()){
            resultList = new ArrayList<>(finalResult.subList((pageNo-1)*pageSize,(pageNo)*pageSize));
        }
        stockEarningPriceVO.setDate(date);
        stockEarningPriceVO.setList(resultList);
        stockEarningPriceVO.setPageNo(pageNo);
        stockEarningPriceVO.setPageSize(pageSize);
        stockEarningPriceVO.setSize(finalResult.size());
        return stockEarningPriceVO;
    }

    @Cacheable(value = "stockInfo::queryLatestStockChangeRate",key = "#pageNo + '-' + #pageSize")
    public List<StockChangeRateVO> queryLatestStockChangeRate(String date, Integer pageNo, Integer pageSize){
        List<StockChangeRateVO> voList = new ArrayList<>();
        List<KItem> kItems = kItemDAO.queryLatestValue();
        List<KItem> secondLatestValue = kItemDAO.querySecondLatestDate();
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(kItems) || CollectionUtils.isEmpty(secondLatestValue) || CollectionUtils.isEmpty(stockItemList)){
            return voList;
        }
        Map<String,KItem> secondLatestMap = secondLatestValue.stream().collect(Collectors.toMap(KItem::getStockId,Function.identity()));
        Map<String,StockItem> stockItemMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId,Function.identity()));
        DecimalFormat df = new DecimalFormat("#0.00");
        for (KItem kItem : kItems){
            StockChangeRateVO stockChangeRateVO = new StockChangeRateVO();
            stockChangeRateVO.setStockId(kItem.getStockId());
            stockChangeRateVO.setDate(kItem.getDate());
            stockChangeRateVO.setPrice(kItem.getClosePrice());
            stockChangeRateVO.setStockName(stockItemMap.get(kItem.getStockId()).getName());
            KItem secondLatestKItem = secondLatestMap.get(kItem.getStockId());
            if (ObjectUtils.isEmpty(secondLatestKItem)){
                continue;
            }
            stockChangeRateVO.setChangeRate(df.format((kItem.getClosePrice() - secondLatestKItem.getClosePrice())/secondLatestKItem.getClosePrice()));
            voList.add(stockChangeRateVO);
        }
        voList = voList.stream().sorted(Comparator.comparing(StockChangeRateVO::getChangeRate).reversed()).collect(Collectors.toList());
        List<StockChangeRateVO> resultList = new ArrayList<>();
        if ((pageNo) * pageSize > voList.size()){
            resultList = new ArrayList<>(voList.subList((pageNo-1)*pageSize,voList.size()-1));
        }
        if ((pageNo) * pageSize < voList.size()){
            resultList = new ArrayList<>(voList.subList((pageNo-1)*pageSize,(pageNo)*pageSize));
        }
        return resultList;
    }

    @Cacheable(value = "stockInfo::queryStockPeriodChangeRate",key = "#pageNo + '-' + #pageSize")
    public List<StockPeriodChangeRateVO> queryStockPeriodChangeRate(String date, Integer pageNo, Integer pageSize) throws Exception{
        List<StockPeriodChangeRateVO> voList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat("yyyyMMdd");
        List<KItem> kItems = kItemDAO.queryLatestValue();
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(kItems) || CollectionUtils.isEmpty(stockItemList)){
            return voList;
        }
        String toDateStr = kItems.get(0).getDate();
        String fromDateStr = "";
        if (StringUtils.isEmpty(date)){
            Date toDate = simpleDateFormat.parse(toDateStr);
            long currmills = toDate.getTime();
            Date fromDate = new Date(currmills - 7 * 24 * 3600 * 1000);
            fromDateStr = simpleDateFormat.format(fromDate);
        }else {
            fromDateStr = date;
        }
        List<KItem> fromDateKItems = kItemDAO.queryKItemsByDate(fromDateStr);
        if (CollectionUtils.isEmpty(fromDateKItems)){
            return voList;
        }
        Map<String,KItem> fromDateKItemMap = fromDateKItems.stream().collect(Collectors.toMap(KItem::getStockId,Function.identity()));
        Map<String,StockItem> stockItemMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId,Function.identity()));
        DecimalFormat df = new DecimalFormat("#0.00");
        for (KItem kItem : kItems){
            if (!fromDateKItemMap.containsKey(kItem.getStockId()) && !stockItemMap.containsKey(kItem.getStockId())){
                continue;
            }
            if(fromDateKItemMap.containsKey(kItem.getStockId())){
                StockPeriodChangeRateVO vo = new StockPeriodChangeRateVO();
                vo.setStockId(kItem.getStockId());
                vo.setStockName(stockItemMap.get(kItem.getStockId()).getName());
                vo.setFromDate(fromDateStr);
                vo.setToDate(toDateStr);
                vo.setPrice(kItem.getClosePrice());
                vo.setChangeRate(df.format(((kItem.getClosePrice() - fromDateKItemMap.get(kItem.getStockId()).getClosePrice())/fromDateKItemMap.get(kItem.getStockId()).getClosePrice()) * 100) + "%");
                voList.add(vo);
            }
        }
        voList = voList.stream().sorted(Comparator.comparing(StockPeriodChangeRateVO::getChangeRate).reversed()).collect(Collectors.toList());
        List<StockPeriodChangeRateVO> resultList = new ArrayList<>();
        if ((pageNo) * pageSize > voList.size()){
            resultList = new ArrayList<>(voList.subList((pageNo-1)*pageSize,voList.size()-1));
        }
        if ((pageNo) * pageSize < voList.size()){
            resultList = new ArrayList<>(voList.subList((pageNo-1)*pageSize,(pageNo)*pageSize));
        }
        return resultList;
    }

    private SimpleDateFormat getSimpleDateFormat(String datePattern){
        SimpleDateFormat sdf = tl.get();
        if (ObjectUtils.isEmpty(sdf)){
            sdf = new SimpleDateFormat(datePattern);
            tl.set(sdf);
        }
        return sdf;
    }
}
