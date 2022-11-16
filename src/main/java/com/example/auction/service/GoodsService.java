package com.example.auction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auction.domain.Goods;
import com.example.auction.persistence.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    public List<Goods> getGoodsList(String roomId){
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        return goodsMapper.selectList(queryWrapper);
    }
    public Goods getwillStartGoods(String roomId){
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
        for(Goods goods:goodsList){
            if(goods.getWillStart()==1){
                return goods;
            }
        }
        return null;
    }

    public void startAuction(String goodsId){
        Goods goods = goodsMapper.selectById(goodsId);
        goods.setIsStart(1);
        goodsMapper.updateById(goods);
    }

    public void closeAuction(String goodsId){
        Goods goods = goodsMapper.selectById(goodsId);
        goods.setIsEnd(1);
        goodsMapper.updateById(goods);
    }

    public void auction(String goodsId, BigDecimal newPrice, String maxname) throws ParseException {
        Lock lock = new ReentrantLock();
        lock.lock();
        Goods goods = goodsMapper.selectById(goodsId);
        if(( newPrice.compareTo(goods.getNewPrice())!= 1)){
            //新价格不大于原价格则return掉
            lock.unlock();
            return;
        }
        goods.setNewPrice(newPrice);
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        goods.setDate(timeStamp);
        goods.setMaxName(maxname);
        goodsMapper.updateById(goods);
        lock.unlock();
    }
}
