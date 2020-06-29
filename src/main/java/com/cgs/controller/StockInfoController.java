package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockInfoService;
import com.cgs.vo.StockInfoVO;
import com.cgs.vo.StockPlateVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api(tags = "股票信息详情接口")
@Slf4j
public class StockInfoController {

    @Autowired
    private StockInfoService stockInfoService;

    @RequestMapping(value = UrlConstant.TOP_VALUE_STOCK_PER_PRICE, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockItemsByIdAndName(@RequestParam(value="stockId",required = false) String stockId,
                                               @RequestParam(value = "stockName", required = false) String stockName){
        Response response = new Response();
        try {
            List<StockInfoVO> list = stockInfoService.queryStockInfoByStockIdAndStockName(stockId,stockName);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,list);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }
}
