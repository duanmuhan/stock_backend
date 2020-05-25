package com.cgs.controller;

import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "股票统计信息接口")
public class StockStaticsController {

    @RequestMapping(value = UrlConstant.FINANCE_INFO, method = RequestMethod.GET)
    @ResponseBody
    public Response queryFinanceInfoByStockId(@ApiParam String stockId){
        return new Response();
    }
}
