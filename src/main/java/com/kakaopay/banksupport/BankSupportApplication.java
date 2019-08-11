package com.kakaopay.banksupport;

import com.kakaopay.banksupport.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class BankSupportApplication {

	private BankService bankService;

	@Autowired
	public BankSupportApplication(BankService bankService) {
		this.bankService = bankService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BankSupportApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void EventListenerExecute(){
		//bankService.setUpInitData();
	}
}
