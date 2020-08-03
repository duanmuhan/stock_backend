package com.cgs.service;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockPlateInfoMappingDAO;
import com.cgs.entity.KItem;
import com.cgs.entity.StockItem;
import com.cgs.entity.StockPlateInfoMapping;
import com.cgs.vo.StockOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlateInfoService {

    @Autowired
    private StockPlateInfoMappingDAO stockPlateInfoMappingDAO;
    @Autowired
    private StockItemDAO stockItemDAO;
    @Autowired
    private KItemDAO kItemDAO;

    public List<StockOverViewVO> queryStockOverviewByPlateId(String plateId){
        List<StockPlateInfoMapping> list = stockPlateInfoMappingDAO.queryPlateInfoMappingByPlateId(plateId);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        List<String> stockIds = list.stream().map(e->e.getStockId()).collect(Collectors.toList());
        List<KItem> kItemList = kItemDAO.queryLatestValue();
        List<KItem> targetKItem = kItemList.stream().filter(e->{
            return stockIds.contains(e.getStockId());
        }).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(kItemList)){
            return null;
        }
        Map<String,KItem> kItemMap = kItemList.stream().collect(Collectors.toMap(KItem::getStockId, Function.identity()));
        List<StockItem> stockItems = stockItemDAO.queryStockListByStockIds(stockIds);
        List<StockOverViewVO> overViewVOList = new ArrayList<>();
        stockItems.stream().forEach(e->{
            StockOverViewVO viewVO = new StockOverViewVO();
            if (kItemMap.containsKey(e.getStockId())){
                KItem kItem = kItemMap.get(e.getStockId());
                viewVO.setPrice(kItem.getClosePrice());
                viewVO.setDealAmount(kItem.getDealAmount());
                viewVO.setStockId(e.getStockId());
                viewVO.setStockName(e.getName());
                viewVO.setDealCash(kItem.getDealCash());
                viewVO.setPriceRate(String.valueOf((kItem.getClosePrice()-kItem.getOpenPrice())/kItem.getOpenPrice() * 100) + "%");
                viewVO.setAmountRate(String.valueOf((kItem.getDealAmount() - kItem.getDealAmount())/kItem.getDealAmount() * 100 + "%"));
                overViewVOList.add(viewVO);
            }
        });
        return overViewVOList;
    }
}
