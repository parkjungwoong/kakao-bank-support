package com.kakaopay.banksupport;

import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Slf4j
@SpringBootApplication
public class BankSupportApplication {

	private BankService bankService;
	private ComSettings comSettings;
	private ResourceLoader resourceLoader;
	private static String filePath;

	@Autowired
	public BankSupportApplication(BankService bankService, ComSettings comSet, ResourceLoader resourceLoader) {
		this.bankService = bankService;
		this.comSettings = comSet;
		this.resourceLoader = resourceLoader;
	}

	public static void main(String[] args) {
		if(args.length > 0) filePath = args[0];
		SpringApplication.run(BankSupportApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void EventListenerExecute() {
		if(filePath != null && filePath.trim().length() > 0) comSettings.setInitDataFilePath(filePath);

		log.info("Load File => [{}]" ,comSettings.getInitDataFilePath());
		bankService.setUpInitData();
	}
}
