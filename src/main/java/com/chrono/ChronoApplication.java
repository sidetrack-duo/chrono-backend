package com.chrono;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chrono.mapper")
public class ChronoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChronoApplication.class, args);
	}

}
