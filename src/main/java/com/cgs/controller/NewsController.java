package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.NewsService;
import com.cgs.vo.news.StockNewsVO;
import io.swagger.annotations.ApiParam;
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
public class NewsController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = UrlConstant.STOCK_INFO_LIST,method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockNews(@ApiParam(value = "新闻时间") @RequestParam(name = "releaseDate") String releaseDate){
        Response response = new Response();
        try {
            List<StockNewsVO> voList = newsService.queryStockInfoByDate(releaseDate);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,voList);
        }catch (Exception e){
            log.error("queryStockKItemByStockId exception :{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }
}
