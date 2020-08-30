package com.cgs.service;

import com.cgs.dao.NewsDAO;
import com.cgs.dao.PlateInfoDAO;
import com.cgs.entity.PlateInfo;
import com.cgs.entity.PolicyInfo;
import com.cgs.vo.news.StockNewsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;
    @Autowired
    private PlateInfoDAO plateInfoDAO;

    public List<StockNewsVO> queryStockInfoByDate(String releaseDate){
       List<PolicyInfo> stockInfo =  newsDAO.queryNewsListBeforeDate(releaseDate);
       if (CollectionUtils.isEmpty(stockInfo)){
           return null;
       }
       List<StockNewsVO> stockNewsVOS = new ArrayList<>();
        stockInfo.stream().forEach(e->{
            StockNewsVO vo = new StockNewsVO();
            vo.setTitle(e.getTitle());
            vo.setPlatform(e.getPlatform());
            vo.setSource(e.getSource());
            vo.setReleaseDate(e.getRelease_date());

            if (StringUtils.isEmpty(e.getTargetPlateId())){
                return;
            }
            String[] plateIds = e.getTargetPlateId().split(",");
            List<String> plateIdList = Arrays.asList(plateIds);
            List<PlateInfo> plateInfoList = plateInfoDAO.batchQueryPlateInfosByPlateIds(plateIdList);
            if (CollectionUtils.isEmpty(plateInfoList)){
                return;
            }
            List<Pair<String,String>> pairList = new ArrayList<>();
            for (PlateInfo plateInfo : plateInfoList){
                Pair<String,String> pair = Pair.of(plateInfo.getPlateId(),plateInfo.getPlateName());
                pairList.add(pair);
            }
            vo.setPlatePairList(pairList);
            stockNewsVOS.add(vo);
        });
        List<StockNewsVO> resultList = stockNewsVOS.stream().filter(e->{
         return !CollectionUtils.isEmpty(e.getPlatePairList());
        }).collect(Collectors.toList());
       return resultList;
    }
}
