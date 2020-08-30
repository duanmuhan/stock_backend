package com.cgs.service;

import com.cgs.constant.StockPriceConstant;
import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.FinanceInfo;
import com.cgs.entity.KItem;
import com.cgs.entity.StockItem;
import com.cgs.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StockStaticsService {

    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则

    @Autowired
    private FinanceInfoDAO financeInfoDAO;
    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

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
            stockChangeVO.setChangeRate(entry.getKey());
            stockChangeVO.setChangeNum(entry.getValue());
            list.add(stockChangeVO);
        }
        if (!StringUtils.isEmpty(date)){
            stockChangeOverViewVO.setDate(date);
        }else {
            stockChangeOverViewVO.setDate(kItems.get(0).getDate());
        }
        list = list.stream().sorted(Comparator.comparing(e->Double.valueOf(e.getChangeRate()))).collect(Collectors.toList());
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
        resultList.add(Pair.of("0-2元", twoCount));
        Long fiveCount = kItems.stream().filter(e->{
            return  e.getClosePrice() > StockPriceConstant.TWO && e.getClosePrice() <= StockPriceConstant.FIVE;
        }).count();
        resultList.add(Pair.of("2-5元", fiveCount));
        Long tenCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FIVE && e.getClosePrice() <= StockPriceConstant.TEN;
        }).count();
        resultList.add(Pair.of("5-10元", tenCount));
        Long twentyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.TEN && e.getClosePrice() <= StockPriceConstant.TWENTY;
        }).count();
        resultList.add(Pair.of("10-20元", twentyCount));
        Long thirtyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.TWENTY && e.getClosePrice() <= StockPriceConstant.THIRTY;
        }).count();
        resultList.add(Pair.of("20-30元", thirtyCount));
        Long fourtyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.THIRTY && e.getClosePrice() <= StockPriceConstant.FORTY;
        }).count();
        resultList.add(Pair.of("30-40元", fourtyCount));
        Long fiftyCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FORTY && e.getClosePrice() <= StockPriceConstant.FIFTY;
        }).count();
        resultList.add(Pair.of("40-50元", fiftyCount));
        Long oneHundredCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FIFTY && e.getClosePrice() <= StockPriceConstant.ONE_HUNDRED;
        }).count();
        resultList.add(Pair.of("50-1百元", oneHundredCount));
        Long twoHundredCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.ONE_HUNDRED && e.getClosePrice() <= StockPriceConstant.TWO_HUNDRED;
        }).count();
        resultList.add(Pair.of("1-2百元", twoHundredCount));
        Long fiveHundredCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.TWO_HUNDRED && e.getClosePrice() <= StockPriceConstant.FIVE_HUNDRED;
        }).count();
        resultList.add(Pair.of("2-5百元", fiveHundredCount));
        Long oneThousandCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.FIVE_HUNDRED && e.getClosePrice() <= StockPriceConstant.ONE_THOUSAND;
        }).count();
        resultList.add(Pair.of("5百-1千元", oneThousandCount));
        Long largerThanOneThousandCount = kItems.stream().filter(e->{
            return e.getClosePrice() > StockPriceConstant.ONE_THOUSAND;
        }).count();
        resultList.add(Pair.of("1千元以上", largerThanOneThousandCount));
        if (!StringUtils.isEmpty(date)){
            vo.setDate(date);
        }else {
            vo.setDate(kItems.get(0).getDate());
        }
        vo.setList(resultList);

        return vo;
    }

    public PageHelperVO queryStockPriceList(String date,String type,Integer pageNo,Integer pageSize){
        PageHelperVO vo = new PageHelperVO();
        List<KItem> kItems = new ArrayList<>();
        if (StringUtils.isEmpty(date)){
            kItems = kItemDAO.queryLatestValue();
        }else {
            kItems = kItemDAO.queryKItemsByDate(date);
        }
        if (StringUtils.isEmpty(type)){
            return vo;
        }
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return vo;
        }
        Map<String,StockItem> stockItemMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId,Function.identity()));
        List<StockPriceVO> stockPriceVOS = new ArrayList<>();
        List<KItem> targetKItems = new ArrayList<>();
        if ("0-2元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() <= StockPriceConstant.TWO;
            }).collect(Collectors.toList());
        }
        if("2-5元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                 return  e.getClosePrice() > StockPriceConstant.TWO && e.getClosePrice() <= StockPriceConstant.FIVE;
            }).collect(Collectors.toList());
        }
        if ("5-10元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.FIVE && e.getClosePrice() <= StockPriceConstant.TEN;
            }).collect(Collectors.toList());
        }
        if ("10-20元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.TEN && e.getClosePrice() <= StockPriceConstant.TWENTY;
            }).collect(Collectors.toList());
        }
        if ("20-30元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.TWENTY && e.getClosePrice() <= StockPriceConstant.THIRTY;
            }).collect(Collectors.toList());
        }
        if ("30-40元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.THIRTY && e.getClosePrice() <= StockPriceConstant.FORTY;
            }).collect(Collectors.toList());
        }
        if ("40-50元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.FORTY && e.getClosePrice() <= StockPriceConstant.FIFTY;
            }).collect(Collectors.toList());
        }
        if ("50-1百元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.FIFTY && e.getClosePrice() <= StockPriceConstant.ONE_HUNDRED;
            }).collect(Collectors.toList());
        }
        if ("1-2百元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.ONE_HUNDRED && e.getClosePrice() <= StockPriceConstant.TWO_HUNDRED;
            }).collect(Collectors.toList());
        }
        if ("2-5百元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.TWO_HUNDRED && e.getClosePrice() <= StockPriceConstant.FIVE_HUNDRED;
            }).collect(Collectors.toList());
        }
        if ("5百-1千元".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.FIVE_HUNDRED && e.getClosePrice() <= StockPriceConstant.ONE_THOUSAND;
            }).collect(Collectors.toList());
        }
        if ("1千元以上".equals(type)){
            targetKItems = kItems.stream().filter(e->{
                return e.getClosePrice() > StockPriceConstant.ONE_THOUSAND;
            }).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(targetKItems)){
            return vo;
        }
        targetKItems.stream().forEach(e->{
            if (!stockItemMap.containsKey(e.getStockId())){
                return;
            }
            StockPriceVO stockPriceVO = new StockPriceVO();
            stockPriceVO.setPrice(e.getClosePrice());
            stockPriceVO.setStockId(e.getStockId());
            stockPriceVO.setStockName(stockItemMap.get(e.getStockId()).getName());
            stockPriceVO.setReleaseDate(e.getDate());
            stockPriceVOS.add(stockPriceVO);
        });
        List<StockPriceVO> resultList = stockPriceVOS.stream().sorted(Comparator.comparing(StockPriceVO::getPrice)).collect(Collectors.toList());
        if (pageNo * pageSize > resultList.size()){
            return vo;
        }
        if ((pageNo+1) * pageSize > stockPriceVOS.size()){
            resultList = new ArrayList<>(resultList.subList((pageNo)*pageSize,resultList.size()));
        }
        if ((pageNo+1) * pageSize < resultList.size()){
            resultList = new ArrayList<>(resultList.subList((pageNo)*pageSize,(pageNo + 1)*pageSize));
        }
        vo.setTotal(stockPriceVOS.size());
        vo.setRows(resultList);
        return vo;
    }

}
