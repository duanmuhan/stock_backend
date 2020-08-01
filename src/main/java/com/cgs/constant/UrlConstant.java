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

    String STOCK_HOLDER_MARKET_VALUE = "/stockholder/marketValue";
    String STOCK_HOLDER_ORDER = "/stockholder/order";
    String STOCK_HOLDER_RATE_HIST = "/stockholder/rate/hist";

    String STOCK_ITEM_VALUE = "/stock/value";
    String STOCK_MARKET_VALUE = "/stock/market/hist";
    String STOCK_CHANGE = "/stock/change/pie";
    String STOCK_PRICE_HIST = "/stock/price/hist";

    String STOCK_INCREASE_RANK="/stock/increase/rank";
    String RATE_OF_INCREASE_RANK="/stock/rate/rank";

    String STOCK_NEWS_LIST = "/stock/news/list";


}