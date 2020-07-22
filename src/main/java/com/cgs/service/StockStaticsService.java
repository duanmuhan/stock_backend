package com.cgs.service;

import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockInfoDAO;
import com.cgs.entity.FinanceInfo;
import com.cgs.entity.KItem;
import com.cgs.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StockStaticsService {

    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则

    @Autowired
    private FinanceInfoDAO financeInfoDAO;
    @Autowired
    private KItemDAO kItemDAO;

    public List<StockBasicVO> queryValuableStockBasicInfo(){
        List<FinanceInfo> financeInfos = financeInfoDAO.queryFinanceInfo();
        if (CollectionUtils.isEmpty(financeInfos)){
            return null;
        }
        Map<String,List<FinanceInfo>> financeMap = financeInfos.parallelStream().collect(Collectors.groupingBy(e->e.getStockId()));
        List<FinanceInfo> financeInfoList = new ArrayList<>();
        for (String key : financeMap.keySet()){
            if (CollectionUtils.isEmpty(financeMap.get(key))){
                continue;
            }
            FinanceInfo financeInfo = financeMap.get(key).parallelStream().sorted(Comparator.comparing(FinanceInfo::getReleaseDate).reversed()).limit(1).findFirst().get();
            financeInfoList.add(financeInfo);
        }
        financeInfoList = financeInfoList.parallelStream().sorted(Comparator.comparing(FinanceInfo::getBasicEarningsPerCommonShare).reversed()).collect(Collectors.toList());
        List<StockBasicVO> voList = new ArrayList<>();
        financeInfoList.stream().forEach(e->{
            StockBasicVO vo = new StockBasicVO();
            vo.setStockId(e.getStockId());
            vo.setBasicEarningsPerCommonShare(StringUtils.isEmpty(e.getBasicEarningsPerCommonShare()) || "--".equals(e.getBasicEarningsPerCommonShare())? 0 : Double.valueOf(e.getBasicEarningsPerCommonShare()));
            voList.add(vo);
        });
        return voList;
    }

    public List<StockPriceAndEarningVO> queryValueStockPerPrice(){
        List<StockBasicVO> list = queryValuableStockBasicInfo();
        List<KItem> valueList = kItemDAO.queryLatestValue();
        Map<String,KItem> valueMap = valueList.stream().collect(Collectors.toMap(KItem::getStockId, Function.identity()));
        List<StockPriceAndEarningVO> resultList = new ArrayList<>();
        list.forEach(e->{
            StockPriceAndEarningVO stockPriceAndEarningVO = new StockPriceAndEarningVO();
            if (valueMap.containsKey(e.getStockId()) && e.getBasicEarningsPerCommonShare()>0){
                stockPriceAndEarningVO.setStockId(e.getStockId());
                stockPriceAndEarningVO.setBasicEarningsPerCommonShare(e.getBasicEarningsPerCommonShare());
                stockPriceAndEarningVO.setPrice(valueMap.get(e.getStockId()).getClosePrice());
                resultList.add(stockPriceAndEarningVO);
            }
        });
        return resultList;
    }

    public StockChangeOverViewVO queryStockChange(String date){
        StockChangeOverViewVO stockChangeOverViewVO = new StockChangeOverViewVO();
        Map<String,Integer> stockChangeMap = new HashMap<>();
        List<KItem> kItems = new ArrayList<>();
        if (StringUtils.isEmpty(date)){
            kItems = kItemDAO.queryLatestValue();
        }else {
            kItems = kItemDAO.queryKItemsByDate(date);
        }
        if (CollectionUtils.isEmpty(kItems)){
            return stockChangeOverViewVO;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        for (KItem kItem : kItems){
            if (ObjectUtils.isEmpty(kItem.getClosePrice()) || ObjectUtils.isEmpty(kItem.getOpenPrice())){
                continue;
            }
            Double rate = (kItem.getClosePrice() - kItem.getOpenPrice())/kItem.getOpenPrice();
            String rateStr = df.format(rate);
            if (stockChangeMap.containsKey(rateStr)){
                int value = stockChangeMap.get(rateStr) + 1;
                stockChangeMap.put(rateStr,value);
            }else {
                int value = 1;
                stockChangeMap.put(rateStr,value);
            }
        }
        List<StockChangeVO> list = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : stockChangeMap.entrySet()){
            StockChangeVO stockChangeVO = new StockChangeVO();
            stockChangeVO.setChangeRate(Double.valueOf(entry.getKey()) * 100 + "%" );
            stockChangeVO.setChangeNum(entry.getValue());
            list.add(stockChangeVO);
        }
        if (!StringUtils.isEmpty(date)){
            stockChangeOverViewVO.setDate(date);
        }else {
            stockChangeOverViewVO.setDate(kItems.get(0).getDate());
        }
        stockChangeOverViewVO.setList(list);
        return stockChangeOverViewVO;
    }

}
