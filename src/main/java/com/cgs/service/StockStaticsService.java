package com.cgs.service;

import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockInfoDAO;
import com.cgs.entity.FinanceInfo;
import com.cgs.entity.KItem;
import com.cgs.vo.StockBasicVO;
import com.cgs.vo.StockPriceAndEarningVO;
import com.cgs.vo.StockValueStaticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private StockInfoDAO stockInfoDAO;

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


    public List<String> queryTestStock(){
        List<FinanceInfo> financeInfoList = financeInfoDAO.queryFinanceInfo();
        if (CollectionUtils.isEmpty(financeInfoList)){
            return new ArrayList<>();
        }
        List<String> resultList  = new ArrayList<>();
        Map<String,List<FinanceInfo>> financeMap = financeInfoList.stream().collect(Collectors.groupingBy(e->e.getStockId()));
        for (String stockId:financeMap.keySet()){
            List<FinanceInfo> tmpList = financeMap.get(stockId).stream().sorted(Comparator.comparing(FinanceInfo::getReleaseDate).reversed()).collect(Collectors.toList());
            if (IsItemsReceivedInAdvance(tmpList) ){
                resultList.add(stockId);
            }
        }
        return resultList;
    }

    private boolean isRateOfMarginIncrease(List<FinanceInfo> tmpList){
        int startIndex=0;
        while (startIndex < tmpList.size()-1){
            int endIndex=startIndex + 1;
            if (!(removeMardrinCharacter(tmpList.get(startIndex).getRateOfMargin()) > removeMardrinCharacter(tmpList.get(endIndex).getRateOfMargin()))){
                return false;
            }
            startIndex ++;
        }
        return true;
    }

    private boolean isDaysInInventoryDecrease(List<FinanceInfo> tmpList){
        int startIndex=0;
        while (startIndex < tmpList.size()-1){
            int endIndex=startIndex + 1;
            if (!(removeMardrinCharacter(tmpList.get(startIndex).getDaysInInventory()) < removeMardrinCharacter(tmpList.get(endIndex).getDaysInInventory()))){
                return false;
            }
            startIndex ++;
        }
        return true;
    }

    private boolean IsItemsReceivedInAdvance(List<FinanceInfo> tmpList){
        int startIndex=0;
        while (startIndex < tmpList.size()-1){
            int endIndex=startIndex + 1;
            if (!(removeMardrinCharacter(tmpList.get(startIndex).getItemsReceivedInAdvance()) > removeMardrinCharacter(tmpList.get(endIndex).getItemsReceivedInAdvance()))){
                return false;
            }
            startIndex ++;
        }
        return true;
    }

    private boolean isGrossRevenueIncrease(List<FinanceInfo> tmpList){
        int startIndex=0;
        while (startIndex < tmpList.size()-1){
            int endIndex=startIndex + 1;
            if (!(removeMardrinCharacter(tmpList.get(startIndex).getGrossRevenue()) > removeMardrinCharacter(tmpList.get(endIndex).getGrossProfit()))){
                return false;
            }
            startIndex ++;
        }
        return true;
    }

    private boolean isAttributableNetProfitNagative(FinanceInfo tmp){
        Double result = removeMardrinCharacter(tmp.getAttributableNetProfit());
        if (result<0){
            return true;
        }
        return false;
    }

    private Double removeMardrinCharacter(String input){
        if (StringUtils.isEmpty(input) || "--".equals(input)){
            return new Double(0);
        }
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(input);
        String result = mat.replaceAll("");
        return Double.valueOf(result);
    }

}
