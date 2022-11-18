package com.example.auction.controller;

import com.example.auction.domain.MyOrder;
import com.example.auction.persistence.GoodsMapper;
import com.example.auction.persistence.MyOrderMapper;
import com.example.auction.persistence.RoomMapper;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/myOrder")
public class MyOrderController {
    @Autowired
    MyOrderService myOderService;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    MyOrderMapper myOrderMapper;
    @Autowired
    RoomService roomService;
    @Autowired
    RoomMapper roomMapper;

    //拍卖方在拍卖结束后生成订单
    @RequestMapping("/genetateOrder")
    public String genetateOrder(HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        MyOrder myOrder = new MyOrder();
        java.util.Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        myOrder.setDate(timeStamp);
        HttpSession session = request.getSession();
        myOrder.setAuctioneerId(roomMapper.selectById((String) session.getAttribute("roomId")).getAuctioneer());
        myOrder.setGoodsId(request.getParameter("goodsId"));
        myOrder.setIsPay(0);
        myOrder.setIsReceive(0);
        myOrder.setPrice(goodsMapper.selectById(request.getParameter("goodsId")).getNewPrice());
        myOrder.setUsername(goodsMapper.selectById(request.getParameter("goodsId")).getMaxName());
        if(goodsMapper.selectById(request.getParameter("goodsId")).getIsEnd()==1){
            myOrderMapper.insert(myOrder);
        }else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.flush();
            out.println("<script>");
            out.println("alert('物品的拍卖尚未结束，请先结束拍卖再生成账单');");
            out.println("history.back();");
            out.println("</script>");
        }
        model.addAttribute("myOrder", myOrder);
        model.addAttribute("goods",goodsMapper.selectById(request.getParameter("goodsId")));
        return "orderInfo.html";
    }
    @RequestMapping("/payOrder")
    public String payOrder(HttpServletRequest request, Model model){
        MyOrder myOrder = myOderService.getorderBygoods(request.getParameter("goodsId"));
        myOrder.setIsPay(1);
        myOrderMapper.updateById(myOrder);

        model.addAttribute("myOrder", myOrder);
        model.addAttribute("goods",goodsMapper.selectById(request.getParameter("goodsId")));
        return "orderInfoManage.html";
    }

    @RequestMapping("/receiveOrder")
    public String receiveOrder(HttpServletRequest request, Model model){
        MyOrder myOrder = myOderService.getorderBygoods(request.getParameter("goodsId"));
        myOrder.setIsReceive(1);
        myOrderMapper.updateById(myOrder);

        model.addAttribute("myOrder", myOrder);
        model.addAttribute("goods",goodsMapper.selectById(request.getParameter("goodsId")));
        return "orderInfoManage.html";
    }
    @RequestMapping("/updateOrder")
    public String updateOrder(HttpServletRequest request, Model model){
        MyOrder myOrder = new MyOrder();
        myOrder.setIsReceive(Integer.valueOf(request.getParameter("isReceive")));
        myOrder.setIsPay(Integer.valueOf(request.getParameter("isPay")));
        myOrderMapper.updateById(myOrder);
        model.addAttribute("myOrder", myOrder);
        model.addAttribute("goods",goodsMapper.selectById(request.getParameter("goodsId")));
        return "orderInfo.html";
    }

    @RequestMapping("/viewaccountOrder")
    public String viewaccountOrder(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute ("username");
        List<MyOrder> orderList = myOderService.getMyOrder(username);
        model.addAttribute("orderList", orderList);
        return "orderList.html";
    }

//    @RequestMapping("/viewauctioneerOrder")
//    public String viewauctioneerOrder(HttpServletRequest request, Model model){
//        String auctioneerId = request.getParameter("username");
//        List<MyOrder> orderList = myOderService.getacutioneerOrder(auctioneerId);
//        model.addAttribute("orderList", orderList);
//        return "orderList.html";
//    }
}
