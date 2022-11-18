package com.example.auction.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auction.domain.MyOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyOrderMapper extends BaseMapper<MyOrder> {
}
