package com.tanmay.excelerate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.tanmay.excelerate.service.AppService;

@SpringBootConfiguration
@EnableAutoConfiguration
@Component
@ComponentScan
public class ExcelerateApplication {
	private static Logger logger = LoggerFactory.getLogger(ExcelerateApplication.class);
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ExcelerateApplication.class);
		ExcelerateApplication mainObj = ctx.getBean(ExcelerateApplication.class);
		mainObj.init();
		logger.debug("<-----------------------Report Generation Completes------------------------------>");
	}

	public void init() {
		logger.debug("<----------------------Report Generation Begins----------------------------------->");
		AppService service=new AppService();
		service.generateReport();
	}
}
