package com.sunilbainsla.kafkastreampoc.faulttolerance.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
public class RetryTransformer<K, V> implements Transformer<K, V, KeyValue<K, V>> {



    private int maxAttempts = -1;
    private int retryCount = 0;
    private RetryTemplate retryTemplate;

    public RetryTransformer(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void init(ProcessorContext context) {
    }

    @Override
    public KeyValue<K, V> transform(K key, V value) {
        try {
            return retryTemplate.execute(retryContext -> {
                // process the message here
                return KeyValue.pair(key, value);
            });
        } catch (Exception ex) {
            if (ex instanceof KafkaException) {
                if (maxAttempts == -1 || retryCount < maxAttempts) {
                    log.warn("Caught KafkaException: {}. Retrying...", ex.getMessage());
                    retryCount++;
                    throw new RetryException(ex.getMessage());
                } else {
                    log.error("Reached max retry attempts. Sending to DLQ.");
                    // throw to DLQ here
                }
            } else {
                log.error("Caught non-KafkaException. Sending to DLQ.", ex);
                // throw to DLQ here
            }
            return null;
        }
    }

    @Override
    public void close() {
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public static class RetryException extends RuntimeException {
        public RetryException(String message) {
            super(message);
        }
    }
}
