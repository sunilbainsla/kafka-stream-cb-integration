package com.sunilbainsla.kafkastreampoc.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class TopicMessage {
    private String id;
    private String message;
}
