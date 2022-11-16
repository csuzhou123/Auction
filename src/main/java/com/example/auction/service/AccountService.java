package com.example.auction.service;

import com.example.auction.domain.Account;
import com.example.auction.persistence.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    AccountMapper accountMapper;

    public String getPassword(String username){
        return accountMapper.selectById(username).getPassword();
    }
}
