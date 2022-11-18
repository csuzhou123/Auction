package com.example.auction.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("myOrder")
public class MyOrder {
    @TableId
    private String orderId;
    private String goodsId;
    private BigDecimal price;
    private Timestamp date;
    private int isPay;
    private int isReceive;
    private String auctioneerId;
    private String username;
}
