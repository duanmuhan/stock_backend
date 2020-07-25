package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockStaticsFormService;
import com.cgs.vo.StockEarningPerPriceVO;
import com.cgs.vo.StockPriceHistVO;
import io.swagger.annotations.Api;
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
@Api(tags = "股票信息统计表格接口")
public class StockStaticsFormController {

    @Autowired
    private StockStaticsFormService stockStaticsFormService;

    @RequestMapping(value = UrlConstant.TOP_VALUE_STOCK_PER_PRICE_FORM, method = RequestMethod.GET)
    @ResponseBody
    public  Response queryStockValuePerPriceForm(@RequestParam(name = "date",required = false) String date) {
        com.cgs.constant.Response response = new Response();
        try {
            List<StockEarningPerPriceVO> list = stockStaticsFormService.queryStockEarningPerPrice(date);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, list);
        } catch (Exception e) {
            log.error("queryTopValueStockPerPrice exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, null);
        }
        return response;
    }
}
