package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockDataService;
import com.cgs.vo.KItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "股票信息接口")
@Slf4j
public class StockDataController {

    public StockDataService stockDataService;

    @RequestMapping(value = UrlConstant.K_ITEM, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockKItemByStockId( @ApiParam @Param("stockId") String stockId){
        Response response = new Response();
        try {
            KItemVO vo = stockDataService.queryKItemByStockId(stockId);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,vo);
        }catch (Exception e){
            log.error("queryStockKItemByStockId exception :{}",e);
        }
        return response;
    }

}
