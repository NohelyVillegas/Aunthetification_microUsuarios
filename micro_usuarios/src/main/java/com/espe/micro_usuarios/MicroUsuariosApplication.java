package com.espe.micro_usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MicroUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroUsuariosApplication.class, args);
	}


}
