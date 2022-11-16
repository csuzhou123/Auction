package com.example.auction.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auction.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
