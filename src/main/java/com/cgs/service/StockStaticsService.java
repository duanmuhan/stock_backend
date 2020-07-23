package com.cgs.service;

import com.cgs.constant.StockPriceConstant;
import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockInfoDAO;
import com.cgs.entity.FinanceInfo;
import com.cgs.entity.KItem;
import com.cgs.vo.*;
import javafx.util.Pair;
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

    public StockPriceHistVO queryStockPriceHist(String date){
        StockPriceHistVO vo = new StockPriceHistVO();
        List<KItem> kItems = new ArrayList<>();
        if (StringUtils.isEmpty(date)){
            kItems = kItemDAO.queryLatestValue();
        }else {
            kItems = kItemDAO.queryKItemsByDate(date);
        }
        if (CollectionUtils.isEmpty(kItems)){
            return vo;
        }
        List<Pair<String,Long>> resultList = new ArrayList<>();
        Long twoCount = kItems.stream().filter(e->{
            return e.getClosePrice() <= StockPriceConstant.TWO;
        }).count();
        resultList.add(new Pair<>("<2元", twoCount));
        Long fiveCount = kItems.stream().filter(e->{
            return  e.getClosePrice() > StockPriceConstant.TWO && e.getClosePrice() <= StockPriceConstant.FIVE;
        }).count();
        resultList.add(new Pair<>("2-5元", fiveCount));
        Long tenCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FIVE && e.getClosePrice() <= StockPriceConstant.TEN;
        }).count();
        resultList.add(new Pair<>("5-10元", tenCount));
        Long twentyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.TEN && e.getClosePrice() <= StockPriceConstant.TWENTY;
        }).count();
        resultList.add(new Pair<>("10-20元", twentyCount));
        Long thirtyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.TWENTY && e.getClosePrice() <= StockPriceConstant.THIRTY;
        }).count();
        resultList.add(new Pair<>("20-30元", thirtyCount));
        Long fourtyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.THIRTY && e.getClosePrice() <= StockPriceConstant.FORTY;
        }).count();
        resultList.add(new Pair<>("30-40元", fourtyCount));
        Long fiftyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FORTY && e.getClosePrice() <= StockPriceConstant.FIFTY;
        }).count();
        resultList.add(new Pair<>("40-50元", fiftyCount));
        Long oneHundredCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FIFTY && e.getClosePrice() <= StockPriceConstant.ONE_HUNDRED;
        }).count();
        resultList.add(new Pair<>("50-1百元", oneHundredCount));
        Long twoHundredCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.ONE_HUNDRED && e.getClosePrice() <= StockPriceConstant.TWO_HUNDRED;
        }).count();
        resultList.add(new Pair<>("1-2百元", twoHundredCount));
        Long fiveHundredCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.TWO_HUNDRED && e.getClosePrice() <= StockPriceConstant.FIVE_HUNDRED;
        }).count();
        resultList.add(new Pair<>("2-5百元", fiveHundredCount));
        Long oneThousandCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FIVE_HUNDRED && e.getClosePrice() <= StockPriceConstant.ONE_THOUSAND;
        }).count();
        resultList.add(new Pair<>("5百-1千元", oneThousandCount));
        Long largerThanOneThousandCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.ONE_THOUSAND;
        }).count();
        resultList.add(new Pair<>("1千元以上", largerThanOneThousandCount));
        if (!StringUtils.isEmpty(date)){
            vo.setDate(date);
        }else {
            vo.setDate(kItems.get(0).getDate());
        }
        vo.setList(resultList);

        return vo;
    }

}
