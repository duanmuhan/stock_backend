package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.PlateInfoService;
import com.cgs.vo.PageHelperVO;
import com.cgs.vo.StockOverViewVO;
import com.cgs.vo.news.StockNewsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@Api(tags = "板块信息接口")
public class PlateInfoController {

    @Autowired
    private PlateInfoService plateInfoService;

    @RequestMapping(value = UrlConstant.PLATE_STOCK_LIST,method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockInfoByPlate(@ApiParam(value = "板块名称") @RequestParam(name = "plateId") String plateId,
                                          @ApiParam(value = "页号") @RequestParam(name = "pageNo") Integer pageNo,
                                          @ApiParam(value = "页码") @RequestParam(name = "pageSize") Integer pageSize){
        Response response = new Response();
        try {
            PageHelperVO vo = plateInfoService.queryStockOverviewByPlateId(plateId,pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,vo);
        }catch (Exception e){
            log.error("queryStockKItemByStockId exception :{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }
}
