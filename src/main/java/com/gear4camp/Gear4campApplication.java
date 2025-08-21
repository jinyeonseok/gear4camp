package com.gear4camp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.gear4camp.mapper")
public class Gear4campApplication {

	public static void main(String[] args) {
		SpringApplication.run(Gear4campApplication.class, args);
	}

}
