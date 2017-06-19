package com.tanmay.excelerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.tanmay.excelerate.dao.AppDao;

@SpringBootConfiguration
@EnableAutoConfiguration
@Component
@ComponentScan
public class ExcelerateApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ExcelerateApplication.class);
		ExcelerateApplication mainObj = ctx.getBean(ExcelerateApplication.class);
		mainObj.init();
		System.out.println("Application exited");
	}

	public void init() {
		AppDao dao=new AppDao();
		System.out.println(dao.executeSQlQueryReturnAsListOfMaps("select id from app_3w_lead limit 10"));
		System.out.println("inside init method");
	}
}
