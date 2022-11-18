package com.example.auction.controller;

import com.example.auction.domain.Goods;
import com.example.auction.persistence.GoodsMapper;
import com.example.auction.service.GoodsService;
import com.example.auction.service.NoticeWebsocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/add")
    public String addGoods(HttpServletRequest request, Model model){
        Goods goods = new Goods();
        goods.setRoomId(request.getParameter("roomId"));
        goods.setIntroduction(request.getParameter("introduction"));
        goods.setOrlPrice(BigDecimal.valueOf(Long.parseLong(request.getParameter("roomId"))));
        goods.setNewPrice(BigDecimal.valueOf(Long.parseLong(request.getParameter("roomId"))));
        goods.setIsStart(0);
        goods.setIsEnd(0);
        goods.setWillStart(0);
        goodsMapper.insert(goods);

        List<Goods> goodsList = goodsService.getGoodsList(request.getParameter("roomId"));
        model.addAttribute("goodsList", goodsList);
        return "goodsManager.html";
    }

    @RequestMapping("/startAuction")
    public String startAuction(HttpServletRequest request, Model model){
        String goodsId = request.getParameter("goodsId");
        Goods goods = goodsMapper.selectById(goodsId);
        goods.setIsStart(1);
        goods.setIsEnd(0);
        goodsMapper.updateById(goods);
        //发送消息
        NoticeWebsocket.sendMessage("start");
        List<Goods> goodsList = goodsService.getGoodsList((String) request.getSession().getAttribute("roomId"));
        model.addAttribute("goodsList", goodsList);
        return "goodsManager.html";
    }

    @RequestMapping("/closeAuction")
    public String closeAuction(HttpServletRequest request, Model model){
        String goodsId = request.getParameter("goodsId");
        Goods goods = goodsMapper.selectById(goodsId);
        goods.setIsStart(0);
        goods.setIsEnd(1);
        //goods.setWillStart(0);
        goodsMapper.updateById(goods);
        NoticeWebsocket.sendMessage("close");
        List<Goods> goodsList = goodsService.getGoodsList((String) request.getSession().getAttribute("roomId"));
        model.addAttribute("goodsList", goodsList);
        return "goodsManager.html";
    }

    @RequestMapping("/viewAuction")
    public String viewAuction(HttpServletRequest request, Model model){
        model.addAttribute("goodsId", request.getParameter("goodsId"));
        return "auction.html";
    }

    @RequestMapping("/auction")
    public String auction(HttpServletRequest request, Model model) throws ParseException {
        Lock lock = new ReentrantLock();
        lock.lock();
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        BigDecimal newPrice = BigDecimal.valueOf(Long.parseLong(request.getParameter("newPrice")));
        goodsService.auction(request.getParameter("goodsId"),newPrice, username);
        NoticeWebsocket.sendMessage("close");
        lock.unlock();
        Goods goods = goodsMapper.selectById(request.getParameter("goodsId"));
        model.addAttribute("goods", goods);
        return "goods.html";
    }
}
