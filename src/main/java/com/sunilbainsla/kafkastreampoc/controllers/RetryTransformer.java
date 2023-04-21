package com.sunilbainsla.kafkastreampoc.controllers;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;



@Slf4j
public class RetryTransformer implements Transformer<String, String, KeyValue<String,String>> {

    private int maxAttempts = -1;
    private int retryCount = 0;

    @Override
    public void init(ProcessorContext context) {
    }

    @Override
    public KeyValue<String, String> transform(String key, String value) {
        try {
            // process the message here
            return KeyValue.pair(key, value);
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