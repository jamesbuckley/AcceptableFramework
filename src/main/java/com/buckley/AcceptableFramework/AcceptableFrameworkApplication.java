package com.buckley.AcceptableFramework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@SpringBootApplication
//@PropertySource("classpath:soteria-web.properties")
public class AcceptableFrameworkApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AcceptableFrameworkApplication.class, args);
	}

	static Properties getProperties() {
		Properties props = new Properties();
		props.put("spring.config.location", "classpath:/,file:/opt/soteria/conf/soteria-content-service-config.properties");
		return props;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder) {
		return springApplicationBuilder
				.sources(AcceptableFrameworkApplication.class)
				.properties(getProperties());
	}
}
