package com.cgs.service;

import com.cgs.constant.AverageType;
import com.cgs.dao.*;
import com.cgs.entity.*;
import com.cgs.vo.*;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private StockTechnologyScoreDAO stockTechnologyScoreDAO;
    @Autowired
    private StockAchievementDAO stockAchievementDAO;
    @Autowired
    private StockTechnologyDAO stockTechnologyDAO;

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

    @Cacheable(value = "stock::average::queryAverageItemByStockId" ,key = "#stockId")
    public AverageVO queryAverageItemByStockId(String stockId) throws ParseException {
        AverageVO vo = new AverageVO();
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        List<AverageItem> fiveDayList = averageDAO.queryAverageItemListByStockId(stockId, AverageType.FIVE_DAYS);
        List<AverageItem> tenDayList = averageDAO.queryAverageItemListByStockId(stockId,AverageType.TEN_DAYS);
        List<AverageItem> twentiesDayList = averageDAO.queryAverageItemListByStockId(stockId,AverageType.TWENTY_DAYS);
        List<AverageItem> sixtiesDayList = averageDAO.queryAverageItemListByStockId(stockId,AverageType.SIXTIES_DAYS);
        List<AverageItem> oneHundredDayList = averageDAO.queryAverageItemListByStockId(stockId,AverageType.ONE_HUNDRED_TWENTY_DAYS);
        vo.setStockId(stockId);
        if (!CollectionUtils.isEmpty(fiveDayList)){
            for (AverageItem item : fiveDayList){
                String date = dateFormat.format(oldDateFormat.parse(item.getDate()));
                item.setDate(date);
            }
        }
        vo.setFiveDayList(fiveDayList);
        if (!CollectionUtils.isEmpty(tenDayList)){
            for (AverageItem item : tenDayList){
                String date = dateFormat.format(oldDateFormat.parse(item.getDate()));
                item.setDate(date);
            }
        }
        vo.setTenDayList(tenDayList);
        if (!CollectionUtils.isEmpty(twentiesDayList)){
            for (AverageItem item : twentiesDayList){
                String date = dateFormat.format(oldDateFormat.parse(item.getDate()));
                item.setDate(date);
            }
        }
        vo.setTwentiesDayList(twentiesDayList);
        if (!CollectionUtils.isEmpty(sixtiesDayList)){
            for (AverageItem item : sixtiesDayList){
                String date = dateFormat.format(oldDateFormat.parse(item.getDate()));
                item.setDate(date);
            }
        }
        vo.setSixtiesDayList(sixtiesDayList);
        if (!CollectionUtils.isEmpty(oneHundredDayList)){
            for (AverageItem item : oneHundredDayList){
                String date = dateFormat.format(oldDateFormat.parse(item.getDate()));
                item.setDate(date);
            }
        }
        vo.setOneHundredAndTwentiesDayList(oneHundredDayList);
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
        StockTechnologyScore stockTechnologyScore = stockTechnologyScoreDAO.queryStockTechnologyScoreByStockId(stockId);
        List<StockTechnologyScore> stockTechnologyScoreList = stockTechnologyScoreDAO.queryLatestStockTechnologyScoreList();
        Long rank = stockTechnologyScoreList.stream().filter(e->{
            return e.getScore() >= stockTechnologyScore.getScore();
        }).count();
        List<StockPlateInfoMapping> plateInfoMappings = stockPlateInfoMappingDAO.queryPlateInfoMappingByStockId(stockId);
        DecimalFormat df = new DecimalFormat("#0.00");
        StockItem item = stockItemDAO.queryStockItemByStockId(stockId);
        viewVO.setPrice(kItems.get(0).getClosePrice());
        viewVO.setDealAmount(kItems.get(0).getDealAmount());
        viewVO.setStockId(stockId);
        viewVO.setStockName(item.getName());
        viewVO.setDealCash(stockInfo.getTotalTransactionAmount());
        viewVO.setPriceRate(df.format((kItems.get(0).getClosePrice()-kItems.get(1).getClosePrice())/kItems.get(0).getOpenPrice() * 100));
        viewVO.setChange(df.format(kItems.get(0).getClosePrice()-kItems.get(1).getClosePrice()) + "(" + viewVO.getPriceRate() + "%" + ")");
        viewVO.setAmountRate(String.valueOf((kItems.get(0).getDealAmount() - kItems.get(0).getDealAmount())/kItems.get(0).getDealAmount() * 100));
        viewVO.setDate(kItems.get(0).getDate());
        viewVO.setAverageTurnoverRate(stockInfo.getAverageTurnoverRate());

        List<StockAchievement> stockAchievements = stockAchievementDAO.queryStockAchievementByStockId(stockId);
        if (!CollectionUtils.isEmpty(stockAchievements)){
            viewVO.setEarningChange(stockAchievements.get(0).getAchievementTitle());
        }
        List<StockTechnology> stockTechnologies = stockTechnologyDAO.queryLatestStockTechnologyListByStockId(stockId);
        if (!CollectionUtils.isEmpty(stockTechnologies)){
            List<StockTechnology> buyStockList = stockTechnologies.stream().filter(e->{
                return "buy".equals(e.getType());
            }).collect(Collectors.toList());
            List<StockTechnology> sellStockList = stockTechnologies.stream().filter(e->{
                return "sell".equals(e.getType());
            }).collect(Collectors.toList());
            List<StockTechnology> eventStockList = stockTechnologies.stream().filter(e->{
                return "event".equals(e.getType());
            }).collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            if (!CollectionUtils.isEmpty(buyStockList)){
                String buyStr = buyStockList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(";<br><br>"));
                stringBuilder.append("<strong>买入信号： </strong>").append(buyStr).append("<br><br>");
            }
            if (!CollectionUtils.isEmpty(sellStockList)){
                String sellStr = sellStockList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(";<br><br>"));
                stringBuilder.append("<strong>卖出信号： </strong>").append(sellStr).append("<br><br>");

            }
            if (!CollectionUtils.isEmpty(eventStockList)){
                String eventStr = eventStockList.stream().map(e->e.getQueryStr()).collect(Collectors.joining(";<br><br>"));
                stringBuilder.append("<strong>股票事件： </strong>").append(eventStr).append("<br><br>");
            }
            viewVO.setTechnologyStr(stringBuilder.toString());
        }

        String plateStr = plateInfoMappings.stream().map(e->e.getPlateName()).collect(Collectors.joining(","));
        viewVO.setPlateInfo("股票板块: " + plateStr);
        viewVO.setScore("股票评分：" + stockTechnologyScore.getScore() + " 评分排名: " + rank);
        return viewVO;
    }
}
