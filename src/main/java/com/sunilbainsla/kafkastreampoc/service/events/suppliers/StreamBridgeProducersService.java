package com.sunilbainsla.kafkastreampoc.service.events.suppliers;

import com.sunilbainsla.kafkastreampoc.kstream.Employee;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class StreamBridgeProducersService {

    private final StreamBridge streamBridge;
    private final Random random;

    public StreamBridgeProducersService(StreamBridge streamBridge) {
        this.random = new Random();
        this.streamBridge = streamBridge;
    }

    public void topicPublisher() {
        Sensor sensor = new Sensor();
        sensor.setId(UUID.randomUUID() + "-v1");
        sensor.setAcceleration(random.nextFloat() * 10);
        sensor.setVelocity(random.nextFloat() * 100);
        sensor.setTemperature(random.nextFloat() * 50);
        log.debug("Producer topic1: {}", sensor);
        Message<String> message = MessageBuilder.withPayload(sensor.toString())
                .setHeader("kafka_key", UUID.randomUUID())
                .build();

        streamBridge.send("topic1", message);
    }

    public void topicPublisher(String id, Payment payment) {

       String publisherTopic;

        if (id.equalsIgnoreCase("startPayment")) {
         publisherTopic = "payment-processor";
            streamBridge.send(publisherTopic, payment);
        }
        if (id.equalsIgnoreCase("startBulk")) {
            publisherTopic = "arrival-notification";
            streamBridge.send(publisherTopic, payment);
        }
        else {
            if (Integer.parseInt(id) <= 9) {

                publisherTopic = "topic" + id;

                String tempMsg = payment.getMessage();
                if (publisherTopic.equalsIgnoreCase("topic4")) {
                    for (int i = 0; i < 2000; i++) {
                        payment.setMessage(tempMsg + " record no " + String.valueOf(i));
                        log.info("Publisher msg for the record \n" + payment.getMessage());
                        streamBridge.send(publisherTopic, payment);
                    }

                } else {
                    streamBridge.send(publisherTopic, tempMsg);
                }
            } else {
                Employee employee = new Employee();
                employee.setDepartment("Engineering");
                employee.setId(UUID.randomUUID().toString());
                employee.setName("Sunil");
                employee.setSalary(20000 + new Random().nextInt());
                publisherTopic = "employee-processor";
                log.debug("Producer {}: {}", publisherTopic, employee);
                streamBridge.send(publisherTopic, employee);
            }
        }

    }
}
