package com.example.auction.controller;

import com.example.auction.domain.Account;
import com.example.auction.domain.Room;
import com.example.auction.persistence.AccountMapper;
import com.example.auction.service.AccountService;
import com.example.auction.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    RoomService roomService;
    //登录
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (password.equals(accountService.getPassword(username))){
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            Account account = accountMapper.selectById(username);
            List<Room> roomList = roomService.getroomList();
            model.addAttribute("roomList", roomList);
            //拍卖方
            if(account.getType()==1){
                return "main.html";
            }
            else{
                return "account.html";
            }
        }else{
            System.out.println("登录失败");
            return "error.html";
        }
    }

    @RequestMapping("/viewregister")
    public String viewregister(HttpServletRequest request){

        return "register.html";
    }
    //注册
    @RequestMapping("/register")
    public String register(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        //名字作为重复校验
        if(accountMapper.selectById(username)!=null){
            //加个错误提示行
            return "index.html";
        }
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String type1 = request.getParameter("type");
        int type2;
        if(type1.equals("Auctioneer")){
            type2=1;
        }else {
            type2=0;
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setType(type2);
        accountMapper.insert(account);
        return "index.html";
    }
}
