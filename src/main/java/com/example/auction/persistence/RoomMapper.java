package com.example.auction.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auction.domain.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {
}
