package com.example.auction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auction.domain.MyOrder;
import com.example.auction.persistence.MyOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyOrderService {
    @Autowired
    MyOrderMapper myOrderMapper;

    public List<MyOrder> getMyOrder(String username){
        QueryWrapper<MyOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return myOrderMapper.selectList(queryWrapper);
    }

//    public List<MyOrder> getacutioneerOrder(String auctioneerId){
//        QueryWrapper<MyOrder> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("auctioneerId", auctioneerId);
//        return myOrderMapper.selectList(queryWrapper);
//    }

    public MyOrder getorderBygoods(String goodsId){
        QueryWrapper<MyOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        return myOrderMapper.selectOne(queryWrapper);
    }
}
