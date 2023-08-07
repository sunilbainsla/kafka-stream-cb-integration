package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class KafkaAsyncProducer {
    public static void main(String[] args) {
        Properties kafkaProperties = new Properties();
        // Configure Kafka properties, e.g., bootstrap.servers, key.serializer, value.serializer, etc.

        KafkaAsyncProducer asyncProducer = new KafkaAsyncProducer(kafkaProperties, "your-topic-name", 300000);

        for (int i = 0; i < 300000; i++) {
            String key = "your-key-" + i;
            Payment payment = createPayment(i); // Your method to create the Payment object

            asyncProducer.sendAsync(key, payment);
        }
    }

    private static Payment createPayment(int i) {
        return new Payment();
    }

    private final KafkaProducer<String, Payment> kafkaProducer;
    private final String kafkaTopicPayment;
    private final int targetRecordCount;
    private int recordCount = 0;

    public KafkaAsyncProducer(Properties kafkaProperties,
            String kafkaTopicPayment,
            int targetRecordCount) {
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties);
        this.kafkaTopicPayment = kafkaTopicPayment;
        this.targetRecordCount = targetRecordCount;
    }

    public void sendAsync(String key, Payment payload) {
        ProducerRecord<String, Payment> record = new ProducerRecord<>(kafkaTopicPayment, key, payload);
        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    System.err.println("Error publishing record: " + exception.getMessage());
                } else {
                    recordCount++;
                    System.out.println("Record published to topic: " + metadata.topic() +
                            ", partition: " + metadata.partition() +
                            ", offset: " + metadata.offset());

                    // Check if the target record count is reached
                    if (recordCount == targetRecordCount) {
                        sendFinalNotification();
                        kafkaProducer.close(); // Close the producer after the final notification
                    }
                }
            }
        });
    }

    private void sendFinalNotification() {
        String finalNotificationKey = "final-notification";

        Payment finalNotificationValue = createPayment(1);

        ProducerRecord<String, Payment> record = new ProducerRecord<>(kafkaTopicPayment, finalNotificationKey, finalNotificationValue);
        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    System.err.println("Error sending final notification: " + exception.getMessage());
                } else {
                    System.out.println("Final notification published to topic: " + metadata.topic() +
                            ", partition: " + metadata.partition() +
                            ", offset: " + metadata.offset());
                }
            }
        });
    }
}
