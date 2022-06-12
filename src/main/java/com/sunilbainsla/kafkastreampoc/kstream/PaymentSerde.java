package com.sunilbainsla.kafkastreampoc.kstream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sunilbainsla.kafkastreampoc.model.kafka.TopicMessage;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.lang.reflect.Type;

public class PaymentSerde implements Serde<TopicMessage> {
    @Override
    public Serializer<TopicMessage> serializer() {
        return new JsonSerializer<>();
    }

    @Override
    public Deserializer<TopicMessage> deserializer() {
        return new JsonDeserializer<>(new TypeReference<TopicMessage>() {},false);

    }
}
