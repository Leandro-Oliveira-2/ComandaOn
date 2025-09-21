package com.lanchonete.lanchon;

import org.springframework.boot.SpringApplication;

public class TestLanchonApplication {

	public static void main(String[] args) {
		SpringApplication.from(LanchonApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
