package com.sunilbainsla.kafkastreampoc.kstream;

import com.google.gson.Gson;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import java.nio.charset.StandardCharsets;

public class PaymentSerdes implements Serde<Payment> {

    private final Gson gson = new Gson();

    @Override
    public Serializer<Payment> serializer() {
        return (topic, data) -> gson.toJson(data).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Deserializer<Payment> deserializer() {
        return (topic, data) -> gson.fromJson(new String(data, StandardCharsets.UTF_8), Payment.class);
    }
}

