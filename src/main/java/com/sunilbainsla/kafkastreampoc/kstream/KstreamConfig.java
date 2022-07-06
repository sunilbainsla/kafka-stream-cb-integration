package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Log4j2
public class KstreamConfig {
    @Bean
    public Function<KStream<String, Payment>, KStream<String, String>> paymentProcessor2() {
        return input -> input.filter(
                        (k, v) ->
                                v.getMessage().startsWith("Sunil")).
                mapValues(
                        (k, v) ->
                                v.getMessage().toUpperCase())
                .selectKey(
                        (k, v) ->
                                v.substring(0, 3))
                .peek((k, v) -> {
                            System.out.println(v);
                        }
                );
    }

    @Bean
    ValueTransformer<Payment, Payment> paymentValueTransformer() {
        return new PaymentValueTransformer();
    }

    @Bean

    public Function<KStream<String, Payment>, KStream<String, Payment>[]> paymentProcessor() {
        return new PaymentTopology();
    }

    @Bean

    public Function<KStream<String, Payment>, KStream<String, Payment>[]> tableProcessor() {
        return new ArbitraryTopology();
    }
}
