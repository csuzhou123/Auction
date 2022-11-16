package com.example.auction;

import com.example.auction.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.text.ParseException;

@SpringBootTest
class AuctionApplicationTests {
    @Autowired
    GoodsService goodsService;

    @Test
    void contextLoads() throws ParseException {
        goodsService.auction("1",new BigDecimal("1000000.0"),"admin");
    }

}
