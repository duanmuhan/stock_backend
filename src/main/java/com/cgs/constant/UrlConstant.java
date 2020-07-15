package com.cgs.constant;

public interface UrlConstant {
    String K_ITEM = "/kitem";
    String FINANCE_INFO = "/financeInfo";
    String STOCK_INFO_LIST ="/stockList";
    String PLATE_INFO_BY_STOCK_ID = "/queryPlateInfoByStockId";

    //财务基本面查询接口
    String TOP_VALUE_STOCK = "/basic";
    String TOP_VALUE_STOCK_PER_PRICE = "/topValuePerPrice";
    String TOP_PLATE_VALUE_STOCK = "/plateBasicStock";
    String OVER_VIEW = "/stock/overview";

    String STOCK_HOLDER_LIST_BY_STOCK_ID = "/stockholder/stockId";

    String STOCK_HOLDER_MARKET_VALUE_PAIR = "/stockholder/marketValue";
    String STOCK_HOLDER_ORDER = "/stockholder/order";
}