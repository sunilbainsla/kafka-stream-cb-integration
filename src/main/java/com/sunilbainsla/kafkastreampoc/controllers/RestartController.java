package com.sunilbainsla.kafkastreampoc.controllers;

import com.sunilbainsla.kafkastreampoc.KafkaStreamPocApplication;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j

public class RestartController {

    private final ConfigurableApplicationContext context;

    public RestartController(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @GetMapping("/restart")
    public void restart() {

    }
}