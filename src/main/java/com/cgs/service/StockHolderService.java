package com.cgs.service;

import com.cgs.constant.StockHolderRateConstant;
import com.cgs.dao.StockHolderDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockHolder;
import com.cgs.entity.StockItem;
import com.cgs.vo.*;
import org.springframework.data.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockHolderService {

    @Autowired
    private StockHolderDAO stockHolderDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public List<StockHolderCoverRateVO> queryStockHolderOrder(HttpServletRequest httpServletRequest){
        List<StockHolder> list =  stockHolderDAO.queryNewestStockHolder();
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        list = list.stream().filter(e->{
            return !StringUtils.isEmpty(e.getTopTenStockFlowHolder()) && !e.getTopTenStockFlowHolder().equals("--");
        }).collect(Collectors.toList());
        List<StockHolderCoverRateVO> coverList = list.stream().map(e->{
            StockHolderCoverRateVO vo = new StockHolderCoverRateVO();
            vo.setStockId(e.getStockId());
            vo.setTopTenStockHolder(e.getTopTenStockHolder());
            return vo;
        }).collect(Collectors.toList());
        coverList = coverList.stream().sorted(Comparator.comparing(StockHolderCoverRateVO::getTopTenStockHolder).reversed()).collect(Collectors.toList());
        return coverList;
    }

    public StockHolderRateVO queryStockHolderRateHist(String date){
        List<StockHolder> list = new ArrayList<>();
        if (StringUtils.isEmpty(date)){
            list = stockHolderDAO.queryNewestStockHolder();
        }else {
            list = stockHolderDAO.queryStockHolderByDate(date);
        }
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        StockHolderRateVO vo = new StockHolderRateVO();
        List<StockHolder> nonEmptyList = list.stream().filter(e->{
            return !StringUtils.isEmpty(e.getTopTenStockHolder()) && !"--".equals(e.getTopTenStockFlowHolder());
        }).collect(Collectors.toList());
        List<Pair<String,Long>> pairList = buildResultPairList(nonEmptyList);
        if (!StringUtils.isEmpty(date)){
            vo.setDate(date);
        }else {
            vo.setDate(list.get(0).getReleaseDate());
        }
        vo.setList(pairList);
        return vo;
    }

    public PageHelperVO queryStockHolderList(String type,Integer pageNo,Integer pageSize){
        PageHelperVO vo = new PageHelperVO();
        List<StockHolder> stockHolderList = stockHolderDAO.queryNewestStockHolder();
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockHolderList) || CollectionUtils.isEmpty(stockItemList)){
            return vo;
        }
        Map<String,String> stockNameMap = stockItemList.stream().collect(Collectors.toMap(StockItem::getStockId,StockItem::getName));
        stockHolderList = stockHolderList.stream().filter(e->{
            return !StringUtils.isEmpty(e.getTopTenStockFlowHolder()) && !e.getTopTenStockFlowHolder().equals("--");
        }).collect(Collectors.toList());
        List<StockHolder> filterList = new ArrayList<>();
        if ("0-5%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.FIVE;
            }).collect(Collectors.toList());
        }
        if ("5%-10%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FIVE && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.TEN;
            }).collect(Collectors.toList());
        }
        if ("10%-20%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.TEN && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.TWENTY;
            }).collect(Collectors.toList());
        }
        if ("20%-30%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.TWENTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.THIRTY;
            }).collect(Collectors.toList());
        }
        if ("30%-40%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.THIRTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.FORTY;
            }).collect(Collectors.toList());
        }
        if ("40%-50%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FORTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.FIFTY;
            }).collect(Collectors.toList());
        }
        if ("50%-60%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FIFTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.SIXTY;
            }).collect(Collectors.toList());
        }
        if ("60%-70%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.SIXTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.SEVENTIES;
            }).collect(Collectors.toList());
        }
        if ("70%-80%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.SEVENTIES && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.EIGHTIES;
            }).collect(Collectors.toList());
        }
        if ("80%-90%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.EIGHTIES && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.NINTIES;
            }).collect(Collectors.toList());
        }
        if ("90%-100%".equals(type)){
            filterList = stockHolderList.stream().filter(e->{
                return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.NINTIES && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.HUNDRED;
            }).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(filterList)){
            return vo;
        }
        List<StockHolder> resultList = new ArrayList<>();
        if (pageNo * pageSize > filterList.size()){
            return vo;
        }
        filterList.stream().sorted(Comparator.comparing(StockHolder::getTopTenStockHolder).reversed()).collect(Collectors.toList());
        if ((pageNo+1) * pageSize > filterList.size()){
            resultList = new ArrayList<>(filterList.subList((pageNo)*pageSize,filterList.size()));
        }
        if ((pageNo+1) * pageSize < filterList.size()){
            resultList = new ArrayList<>(filterList.subList((pageNo)*pageSize,(pageNo + 1)*pageSize));
        }
        List<StockHolderVO> resultVoList = new ArrayList<>();
        resultList.forEach(e->{
            StockHolderVO stockHolderVO = new StockHolderVO();
            stockHolderVO.setStockId(e.getStockId());
            stockHolderVO.setStockName(stockNameMap.containsKey(e.getStockId()) ? stockNameMap.get(e.getStockId()) : "--");
            stockHolderVO.setNumberOfShareholders(e.getNumberOfShareholders());
            stockHolderVO.setPerCapitaTradableShares(e.getPerCapitaTradableShares());
            stockHolderVO.setLastChange(e.getLastChange());
            stockHolderVO.setStockConvergenceRate(e.getStockConvergenceRate());
            stockHolderVO.setPrice(e.getPrice());
            stockHolderVO.setPerCapitaHoldingAmount(e.getPerCapitaHoldingAmount());
            stockHolderVO.setTopTenStockHolder(e.getTopTenStockHolder());
            stockHolderVO.setTopTenStockFlowHolder(e.getTopTenStockFlowHolder());
            stockHolderVO.setReleaseDate(e.getReleaseDate());
            resultVoList.add(stockHolderVO);
        });
        vo.setTotal(filterList.size());
        vo.setRows(resultVoList);
        return vo;
    }

    private List<Pair<String,Long>> buildResultPairList(List<StockHolder> list){
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        List<Pair<String,Long>> pairList = new ArrayList<>();
        Long five = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.FIVE;
        }).count();
        pairList.add(Pair.of("0-5%",five));
        Long ten = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FIVE && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.TEN;
        }).count();
        pairList.add(Pair.of("5%-10%",ten));
        Long twenty = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.TEN && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.TWENTY;
        }).count();
        pairList.add(Pair.of("10%-20%",twenty));
        Long thirty = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.TWENTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.THIRTY;
        }).count();
        pairList.add(Pair.of("20%-30%",thirty));
        Long forty = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.THIRTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.FORTY;
        }).count();
        pairList.add(Pair.of("30%-40%",forty));
        Long fifty = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FORTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.FIFTY;
        }).count();
        pairList.add(Pair.of("40%-50%",fifty));
        Long sixty = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FIFTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.SIXTY;
        }).count();
        pairList.add(Pair.of("50%-60%",sixty));
        Long seventies = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.FIFTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.SIXTY;
        }).count();
        pairList.add(Pair.of("60%-70%",seventies));
        Long eighties = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.SIXTY && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.SEVENTIES;
        }).count();
        pairList.add(Pair.of("70%-80%",eighties));
        Long ninties = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.SEVENTIES && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.EIGHTIES;
        }).count();
        pairList.add(Pair.of("80%-90%",ninties));
        Long hundred = list.stream().filter(e->{
            return Double.valueOf(e.getTopTenStockHolder()) > StockHolderRateConstant.NINTIES && Double.valueOf(e.getTopTenStockHolder()) <= StockHolderRateConstant.NINTIES;
        }).count();
        pairList.add(Pair.of("90%-100%",ninties));
        return pairList;
    }


}
