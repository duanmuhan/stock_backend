package com.cgs.constant;

public interface UrlConstant {
    String K_ITEM = "/kitem";
    String AVERAGE_ITEM = "/average";
    String FINANCE_INFO = "/financeInfo";
    String STOCK_INFO_LIST ="/stockList";
    String PLATE_INFO_BY_STOCK_ID = "/queryPlateInfoByStockId";

    //财务基本面查询接口
    String TOP_VALUE_STOCK = "/basic";
    String TOP_VALUE_STOCK_PER_PRICE = "/topValuePerPrice";
    String TOP_VALUE_STOCK_PER_PRICE_FORM = "/topValuePerPrice/form";
    String TOP_PLATE_VALUE_STOCK = "/plateBasicStock";
    String OVER_VIEW = "/stock/overview";

    String STOCK_HOLDER_LIST_BY_STOCK_ID = "/stockholder/stockId";

    String STOCK_HOLDER_MARKET_VALUE_TYPE = "/stockholder/marketValue/type";
    String STOCK_HOLDER_ORDER = "/stockholder/order";
    String STOCK_HOLDER_RATE_HIST = "/stockholder/rate/hist";

    String STOCK_ITEM_VALUE = "/stock/value";
    String STOCK_MARKET_VALUE = "/stock/market/hist";
    String STOCK_MARKET_TYPE = "/stock/market/type";


    String STOCK_CHANGE = "/stock/change/hist";
    String STOCK_PRICE_HIST = "/stock/price/hist";
    String STOCK_PRICE_TYPE = "/stock/price/type";

    String STOCK_INCREASE_RANK="/stock/increase/rank";
    String RATE_OF_INCREASE_RANK="/stock/rate/rank";

    String STOCK_NEWS_LIST = "/stock/news/list";
    String PLATE_STOCK_LIST = "/plate/stock/list";

    String STOCK_ACHIEVEMENT_LIST = "/stock/achievement/list";
    String STOCK_ACHIEVEMENT_GROUP = "/stock/achievement/group";
    String STOCK_ACHIEVEMENT_TYPE = "/stock/achievement/type/list";

    String STOCK_TECHNOLOGY_SCORE = "/stock/technology/score";
    String STOCK_TECHNOLOGY_SCORE_BY_STOCK_ID = "/stock/technology/score/stockId";

    String STOCK_TECHNOLOGY = "/stock/technology";
    String STOCK_TECHNOLOGY_BY_STOCK_ID = "/stock/technology/stockId";
    String STOCK_TECHNOLOGY_BY_TYPE= "/stock/technology/type";
}