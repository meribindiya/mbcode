package com.mbemployee.empapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@ComponentScan("com.mbemployee")
@Import({CommonConfiguration.class})
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
public class EmpapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpapiApplication.class, args);
	}
}
