package com.sunilbainsla.kafkastreampoc;

import com.sunilbainsla.kafkastreampoc.controllers.RestartController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class KafkaStreamPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamPocApplication.class, args);
    }
    @Bean
    public RestartController restartController(ConfigurableApplicationContext context) {
        return new RestartController(context);
    }
}