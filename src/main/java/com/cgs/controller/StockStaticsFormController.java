package com.cgs.controller;

import com.cgs.ResponseUtils;
import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;
import com.cgs.constant.UrlConstant;
import com.cgs.service.StockAchievementService;
import com.cgs.service.StockStaticsFormService;
import com.cgs.vo.PageHelperVO;
import com.cgs.vo.StockAchievementGroupVO;
import com.cgs.vo.StockEarningPriceVO;
import com.cgs.vo.forms.StockAchievementVO;
import com.cgs.vo.forms.StockChangeRateVO;
import com.cgs.vo.forms.StockPeriodChangeRateVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "股票信息统计表格接口")
public class StockStaticsFormController {

    @Autowired
    private StockStaticsFormService stockStaticsFormService;
    @Autowired
    private StockAchievementService stockAchievementService;

    @RequestMapping(value = UrlConstant.TOP_VALUE_STOCK_PER_PRICE_FORM, method = RequestMethod.GET)
    @ResponseBody
    public  Response queryStockValuePerPriceForm(@RequestParam(name = "date",required = false) String date,
                                                 @RequestParam(name = "pageNo") Integer pageNo,
                                                 @RequestParam(name = "pageSize") Integer pageSize) {
        com.cgs.constant.Response response = new Response();
        try {
            PageHelperVO vo = stockStaticsFormService.queryStockEarningPerPrice(date,pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        } catch (Exception e) {
            log.error("queryTopValueStockPerPrice exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, null);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_INCREASE_RANK,method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockIncreaseRank(@RequestParam(name = "date",required = false) String date,
                                           @RequestParam(name = "pageNo") Integer pageNo,
                                           @RequestParam(name = "pageSize") Integer pageSize){

        Response response = new Response();
        try {
            PageHelperVO vo = stockStaticsFormService.queryLatestStockChangeRate(date,pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        } catch (Exception e) {
            log.error("queryTopValueStockPerPrice exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, null);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.RATE_OF_INCREASE_RANK, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockIncreaseRateRank(@RequestParam(name = "date",required = false) String date,
                                               @RequestParam(name = "pageNo") Integer pageNo,
                                               @RequestParam(name = "pageSize") Integer pageSize){
        Response response = new Response();
        try {
            PageHelperVO vo = stockStaticsFormService.queryStockPeriodChangeRate(date,pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        } catch (Exception e) {
            log.error("queryTopValueStockPerPrice exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, e);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_ACHIEVEMENT_LIST, method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockAchievementList(@RequestParam(name = "pageNo") Integer pageNo,
                                              @RequestParam(name = "pageSize")Integer pageSize){
        Response response = new Response();
        try {
            PageHelperVO vo = stockAchievementService.queryStockAchievementByPage(pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        } catch (Exception e) {
            log.error("queryStockAchievementList exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, e);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_ACHIEVEMENT_GROUP,method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockAchievementGroup(HttpServletRequest httpServletRequest){
        Response response = new Response();
        try {
            StockAchievementGroupVO vo = stockAchievementService.queryStockAchievementGroup();
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        }catch (Exception e){
            log.error("queryStockAchievementGroup exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, e);
        }
        return response;
    }

    @RequestMapping(value = UrlConstant.STOCK_ACHIEVEMENT_TYPE,method = RequestMethod.GET)
    @ResponseBody
    public Response queryStockAchievementType(@RequestParam("type") String type,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        Response response = new Response();
        try {
            PageHelperVO vo = stockAchievementService.queryStockAchievementListByType(type,pageNo,pageSize);
            response = ResponseUtils.buildResponseByCode(ErrorCode.OK, vo);
        }catch (Exception e){
            log.error("queryStockAchievementType exception:{}", e);
            response = ResponseUtils.buildResponseByCode(ErrorCode.EXCEPTION, e);
        }
        return response;
    }
}
