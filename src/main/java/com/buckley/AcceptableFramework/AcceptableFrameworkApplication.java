package com.buckley.AcceptableFramework;

import com.buckley.AcceptableFramework.utils.RequestResponseWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
@PropertySource("classpath:request.properties")
public class AcceptableFrameworkApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AcceptableFrameworkApplication.class, args);
		RequestResponseWriter writer = context.getBean(RequestResponseWriter.class);
	}
}
