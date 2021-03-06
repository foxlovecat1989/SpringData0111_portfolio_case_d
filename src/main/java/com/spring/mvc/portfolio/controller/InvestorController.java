package com.spring.mvc.portfolio.controller;

import com.spring.mvc.portfolio.entities.Investor;
import com.spring.mvc.portfolio.entities.Watch;
import com.spring.mvc.portfolio.service.EmailService;
import com.spring.mvc.portfolio.service.PortfolioService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/portfolio/investor")
public class InvestorController {
    
    @Autowired
    private  PortfolioService portfolioService;
    
    @Autowired
    private  EmailService emailService;
    
    @GetMapping(value = {"/", "/query"})
    public List<Investor> query(){
        return portfolioService.getInvestorRepository().findAll();
    }
    
        // 新增
    @PostMapping(value = {"/", "/add"})
    public Investor add(@RequestBody Map<String, String> jsonMap) {
        Investor investor = new Investor();
        investor.setUsername(jsonMap.get("username"));
        investor.setPassword(jsonMap.get("password"));
        investor.setEmail(jsonMap.get("email"));
        investor.setBalance(Integer.parseInt(jsonMap.get("balance")));
        investor.setPass(Boolean.FALSE);
        // 設定認證碼
        investor.setCode(Integer.toHexString(investor.hashCode()));
        
        // 存檔 Investor
        portfolioService.getInvestorRepository().save(investor);
        // 存檔 Watch
        Watch watch = new Watch(investor.getUsername() + "投資組合", investor);
        portfolioService.getWatchRepository().save(watch);
        
        
        // 發送認證信
        emailService.send(investor);
        
        return investor;
    }
}
