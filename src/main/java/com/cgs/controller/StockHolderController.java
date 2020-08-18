package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockHolderService;
import com.cgs.vo.PageHelperVO;
import com.cgs.vo.StockHolderCoverRateVO;
import com.cgs.vo.StockHolderRateVO;
import com.cgs.vo.StockInfoVO;
import io.swagger.annotations.Api;
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
@Api(tags = "股东信息接口")
@Slf4j
public class StockHolderController {

    @Autowired
    private StockHolderService stockHolderService;

    @RequestMapping(value = UrlConstant.STOCK_HOLDER_ORDER, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockHolderOrder(HttpServletRequest httpServletRequest){

        Response response = new Response();
        try {
            List<StockHolderCoverRateVO> list =  stockHolderService.queryStockHolderOrder(httpServletRequest);;
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,list);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_HOLDER_RATE_HIST, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockHolderRateHist(@RequestParam(name = "date",required=false) String date){
        Response response = new Response();
        try {
            StockHolderRateVO vo =  stockHolderService.queryStockHolderRateHist(date);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,vo);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_HOLDER_MARKET_VALUE_TYPE, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockHolderListByType(@RequestParam(name = "type") String type,
                                               @RequestParam(name = "pageNo") Integer pageNo,
                                               @RequestParam(name = "pageSize") Integer pageSize
                                               ){
        Response response = new Response();
        try {
            PageHelperVO vo =  stockHolderService.queryStockHolderList(type,pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,vo);
        }catch (Exception e){
            log.error("queryPlateInfoByStockId exception:{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,e.getMessage());
        }
        return response;

    }
}
