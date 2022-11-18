package com.example.auction.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("goods")
public class Goods {
    @TableId
    private String goodsId;
    private String roomId;
    private String introduction;
    private BigDecimal orlPrice;
    private BigDecimal newPrice;
    private int willStart;
    private Timestamp date;
    private int isStart;
    private int isEnd;
    private String maxName;
    @TableField(exist = false)
    private Boolean flag;
}
