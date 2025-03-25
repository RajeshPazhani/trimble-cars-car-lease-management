package com.trimblecars;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.trimblecars")
public class TrimblecarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrimblecarsApplication.class, args);
	}

}
