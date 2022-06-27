package com.sunilbainsla.kafkastreampoc.kstream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class PaymentSerde implements Serde<Payment> {
    @Override
    public Serializer<Payment> serializer() {
        return new JsonSerializer<>();
    }

    @Override
    public Deserializer<Payment> deserializer() {
        return new JsonDeserializer<>(new TypeReference<Payment>() {},false);

    }
}
