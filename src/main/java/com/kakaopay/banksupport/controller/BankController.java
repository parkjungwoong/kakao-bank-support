package com.kakaopay.banksupport.controller;

import com.kakaopay.banksupport.config.security.dto.UserContext;
import com.kakaopay.banksupport.dto.req.LbsUpdateDTO;
import com.kakaopay.banksupport.dto.res.ComDTO;
import com.kakaopay.banksupport.dto.res.LbsResDTO;
import com.kakaopay.banksupport.dto.SearchDTO;
import com.kakaopay.banksupport.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private BankService bankService;

    //검색
    @GetMapping("/search")
    public LbsResDTO search(@RequestBody(required = false) SearchDTO searchDTO) {
        return bankService.search(searchDTO);
    }

    //수정
    @PostMapping("/update")
    public ComDTO update(@RequestBody LbsUpdateDTO dto, @AuthenticationPrincipal UserContext userContext) {
        return bankService.update(dto, userContext);
    }
}
