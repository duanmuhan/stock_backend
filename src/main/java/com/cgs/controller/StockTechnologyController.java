package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockTechnologyService;
import com.cgs.vo.forms.StockTechnologyScoreVO;
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
public class StockTechnologyController {
    @Autowired
    private StockTechnologyService stockTechnologyService;

    @RequestMapping(value = UrlConstant.STOCK_TECHNOLOGY_SCORE, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockTechnologyScoreList(@RequestParam(name = "pageNo") Integer pageNo,
                                                  @RequestParam(name = "pageSize")Integer pageSize){
        Response response = new Response();
        try {
            List<StockTechnologyScoreVO> list = stockTechnologyService.queryLatestStockTechnologyScoreByPage(pageSize,pageNo);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, list);
        }catch (Exception e){
            log.error("queryStockTechnologyScoreList exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, e);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_TECHNOLOGY_SCORE_BY_STOCK_ID, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockTechnologyScoreList(@RequestParam(name = "stockId") String stockId){
        Response response = new Response();
        try {
            StockTechnologyScoreVO vo = stockTechnologyService.queryStockTechnologyByStockId(stockId);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        }catch (Exception e){
            log.error("queryStockTechnologyScoreList exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, e);
        }
        return response;
    }
}
