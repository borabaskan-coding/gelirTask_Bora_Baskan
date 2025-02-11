package com.borabaskan.gelirTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.borabaskan.gelirTask")//springboot uygulamasini baslatmak icin
@EntityScan(basePackages = {"com.borabaskan.gelirTask"})//Entity anatasyonunu bulmabilmesi icin tarattik
//@ComponentScan(basePackages = {"com.borabaskan.gelirTask"})
@EnableJpaRepositories(basePackages = {"com.borabaskan.gelirTask"}) //Jpa repositoryleri projede tanimladik
public class GelirTaskApplication {

	//Uygulama bu siniftan ayaga kalkacak
	public static void main(String[] args) {
		SpringApplication.run(GelirTaskApplication.class, args);
	}

}
