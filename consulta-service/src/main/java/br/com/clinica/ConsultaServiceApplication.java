package br.com.clinica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsultaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultaServiceApplication.class, args);
    }

    // Bean essencial para permitir chamadas HTTP síncronas entre microsserviços
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}