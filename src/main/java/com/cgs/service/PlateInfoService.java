package com.cgs.service;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockPlateInfoMappingDAO;
import com.cgs.entity.KItem;
import com.cgs.entity.StockItem;
import com.cgs.entity.StockPlateInfoMapping;
import com.cgs.vo.PageHelperVO;
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

    public PageHelperVO queryStockOverviewByPlateId(String plateId,Integer pageNo,Integer pageSize){
        Integer startIndex = pageNo * pageSize;
        List<StockPlateInfoMapping> list = stockPlateInfoMappingDAO.queryPlateInfoMappingByPlateId(plateId,startIndex,pageSize);
        Integer count = stockPlateInfoMappingDAO.queryPlateInfoMappingCountByPlate(plateId);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        List<String> stockIds = list.stream().map(e->e.getStockId()).collect(Collectors.toList());
        List<KItem> kItemList = kItemDAO.queryLatestValue();
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
                viewVO.setDate(kItem.getDate());
                overViewVOList.add(viewVO);
            }
        });
        PageHelperVO vo = new PageHelperVO();
        vo.setRows(overViewVOList);
        vo.setTotal(count);
        return vo;
    }
}
