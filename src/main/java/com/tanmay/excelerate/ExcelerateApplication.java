package com.tanmay.excelerate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.tanmay.excelerate.service.AppServiceImpl;

@SpringBootConfiguration
@EnableAutoConfiguration
@Component
@ComponentScan
public class ExcelerateApplication implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(ExcelerateApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ExcelerateApplication.class, args);
	}

	@Autowired
	private AppServiceImpl appService;

	@Override
	public void run(String... arg0) throws Exception {
		logger.debug("<-----------------------------------------------Initiating report Generation------------------------------------------------->");
		appService.generate();
		logger.debug("<-----------------------------------------------Report Generation Completes-------------------------------------------------->");
	}
}
