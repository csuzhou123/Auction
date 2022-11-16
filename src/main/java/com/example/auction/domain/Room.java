package com.example.auction.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//拍卖场
@Data
@TableName("room")
public class Room {
    @TableId
    private String roomId;
    private String password;
    private String address;
    private String auctioneer;
    private int isStart;
}
