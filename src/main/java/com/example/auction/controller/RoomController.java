package com.example.auction.controller;

import com.example.auction.domain.Goods;
import com.example.auction.domain.Room;
import com.example.auction.persistence.AccountMapper;
import com.example.auction.persistence.RoomMapper;
import com.example.auction.service.AccountService;
import com.example.auction.service.GoodsService;
import com.example.auction.service.MyOrderService;
import com.example.auction.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;


@Controller
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MyOrderService myOderService;

    @RequestMapping("/viewnewRoom")
    public String viewnewRoom(){
        return "newRoom.html";
    }
    @RequestMapping("/newRoom")
    public String newRoom(HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //如果是拍卖方的话
        if(accountMapper.selectById((String)session.getAttribute("username")).getType()==1){
            Room room = new Room();
            room.setAddress(request.getParameter("address"));
            room.setAuctioneer(request.getParameter("auctioneer"));
            room.setIsStart(Integer.valueOf(request.getParameter("isStart")));
            Random random = new Random();
            String newpassword = "";
            for(int i=0;i<6;i++){
                newpassword = newpassword + random.nextInt(10);
            }
            room.setPassword(newpassword);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.flush();
            out.println("<script>");
            out.println("alert('"+newpassword+"');");
            out.println("history.back();");
            out.println("</script>");
            roomService.newRoom(room);
            List<Room> roomList = roomService.getroomList();
            model.addAttribute("roomList", roomList);
            return "main.html";
        }else {
            return "error.html";
        }

    }

    @RequestMapping("/viewEnter")
    public String enter(){
        return "viewEnter.html";
    }
    @RequestMapping("/enter")
    public String enter(HttpServletRequest request, Model model){
        String roomId = request.getParameter("roomId");
        String password = request.getParameter("password");
        Room room = roomMapper.selectById(roomId);
        HttpSession session = request.getSession();
        session.setAttribute("roomId", roomId);
        Goods goods = new Goods();
        List<Goods> goodsList;
        if(password.equals(room.getPassword())){
            if(accountMapper.selectById((String)session.getAttribute("username")).getType()==1){
                goodsList = goodsService.getGoodsList(roomId);
                for(Goods good:goodsList){
                    boolean flag = myOderService.getorderBygoods(good.getGoodsId())==null?false:true;
                    good.setFlag(flag);
                }
                model.addAttribute("goodsList", goodsList);
                return "goodsManager.html";
            }
            else{
                goods = goodsService.getwillStartGoods(roomId);
                model.addAttribute("goods", goods);
                return "goods.html";
            }
        }else {
            return "error.html";
        }
    }

}
