package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockDataService;
import com.cgs.vo.KItemVO;
import com.cgs.vo.StockBasicVO;
import com.cgs.vo.StockPlateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Api(tags = "股票信息接口")
@Slf4j
public class StockDataController {

    @Autowired
    public StockDataService stockDataService;

    @RequestMapping(value = UrlConstant.K_ITEM, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockKItemByStockId(@ApiParam(value = "股票id") @RequestParam(name="stockId")  String stockId){
        Response response = new Response();
        try {
            KItemVO vo = stockDataService.queryKItemByStockId(stockId);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,vo);
        }catch (Exception e){
            log.error("queryStockKItemByStockId exception :{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.PLATE_INFO_BY_STOCK_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryPlateInfoByStockId(@ApiParam(value = "股票id") @RequestParam(name = "stockId") String stockId){
        Response response = new Response();
        try {
            StockPlateVO vo = stockDataService.queryStockPlateInfoByStockId(stockId);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,vo);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.TOP_VALUE_STOCK, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockByBasicInfo(HttpServletRequest httpServletRequest){
        Response response = new Response();
        try {
            List<StockBasicVO> list = stockDataService.queryValuableStockBasicInfo();
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,list);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }

}
