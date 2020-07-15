package com.cgs.controller;

import com.cgs.constant.Response;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "股东信息接口")
@Slf4j
public class StockHolderController {


    public Response queryStockHolderMarketPair(HttpServletRequest httpServletRequest){
        return new Response();
    }
}
