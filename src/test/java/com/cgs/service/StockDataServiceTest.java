package com.cgs.service;

import com.cgs.vo.KItemVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockDataServiceTest {

    @Autowired
    private StockDataService stockDataService;
    @Autowired
    private StockStaticsService stockStaticsService;

    @Test
    public void testQueryKItemByStockId(){
        KItemVO vo = stockDataService.queryKItemByStockId("300064");
    }

}
