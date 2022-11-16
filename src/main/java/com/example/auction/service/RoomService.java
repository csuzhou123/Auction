package com.example.auction.service;

import com.example.auction.domain.Room;
import com.example.auction.persistence.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomMapper roomMapper;

    public List<Room> getroomList(){
        return roomMapper.selectList(null);
    }

    public void newRoom(Room room){
        roomMapper.insert(room);
    }
}
