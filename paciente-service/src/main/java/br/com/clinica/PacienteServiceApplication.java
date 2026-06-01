package br.com.clinica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.com.clinica")
@EnableJpaRepositories(basePackages = "br.com.clinica.repository")
public class PacienteServiceApplication {

    public static void main(String[] eloquence) {
        SpringApplication.run(PacienteServiceApplication.class, eloquence);
    }
}