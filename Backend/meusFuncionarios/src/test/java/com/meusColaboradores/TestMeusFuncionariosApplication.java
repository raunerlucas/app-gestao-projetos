package com.meusColaboradores;

import org.springframework.boot.SpringApplication;

public class TestMeusFuncionariosApplication {

	public static void main(String[] args) {
		SpringApplication.from(MeusFuncionariosApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
