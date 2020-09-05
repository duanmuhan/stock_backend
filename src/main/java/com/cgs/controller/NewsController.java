package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.NewsService;
import com.cgs.vo.news.StockNewsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@Api(tags = "股票新闻信息接口")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = UrlConstant.STOCK_NEWS_LIST,method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockNews(){
        Response response = new Response();
        try {
            String releaseDate = "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isEmpty(releaseDate)){
                releaseDate = simpleDateFormat.format(new Date());
            }
            List<StockNewsVO> voList = newsService.queryStockInfoByDate(releaseDate);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK,voList);
        }catch (Exception e){
            log.error("queryStockKItemByStockId exception :{}",e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION,null);
        }
        return response;
    }
}
