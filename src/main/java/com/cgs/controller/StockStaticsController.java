package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockStaticsService;
import com.cgs.vo.StockBasicVO;
import com.cgs.vo.StockPriceAndEarningVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Api(tags = "股票统计信息接口")
@Slf4j
public class StockStaticsController {

    @Autowired
    private StockStaticsService stockStaticsService;

    @RequestMapping(value = UrlConstant.FINANCE_INFO, method = RequestMethod.GET)
    @ResponseBody
    public Response queryFinanceInfoByStockId(HttpServletRequest httpServletRequest){

        return new Response();
    }

    @RequestMapping(value = UrlConstant.TOP_VALUE_STOCK, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockByBasicInfo(HttpServletRequest httpServletRequest){
        Response response = new Response();
        try {
            List<StockBasicVO> list = stockStaticsService.queryValuableStockBasicInfo();
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,list);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.TOP_VALUE_STOCK_PER_PRICE, method = RequestMethod.GET)
    @ResponseBody
    public Response queryTopValueStockPerPrice(HttpServletRequest httpServletRequest){
        Response response = new Response();
        try {
            List<StockPriceAndEarningVO> list = stockStaticsService.queryTopValueStockPerPrice();
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,list);
        }catch (Exception e){
            log.error("queryTopValueStockPerPrice exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }
}
