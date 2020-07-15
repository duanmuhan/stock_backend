package com.cgs.controller;

import com.cgs.constant.Response;
import com.cgs.service.StockHolderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "股东信息接口")
@Slf4j
public class StockHolderController {

    @Autowired
    private StockHolderService stockHolderService;

    public Response queryStockHolderMarketPair(HttpServletRequest httpServletRequest){
        return new Response();
    }
}
