package com.cgs.esdao;

import com.cgs.esdao.po.StockItemEsPo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author caoguangshu
 * @date 2021/2/12
 * @time 9:45 下午
 */
@Repository
public class StockItemEsDao extends BaseEsDAO {

    private static String INDEX_NAME = "stock_item_index";

    private static String TYPE_NAME = "stock_item_type";

    public StockItemEsDao() {
        super(INDEX_NAME, TYPE_NAME);
    }

    public List<StockItemEsPo> queryStockItemListByStockId(String stockId){
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
        booleanQueryBuilder.should(QueryBuilders.termsQuery("stockId",stockId));

        return null;
    }
}
