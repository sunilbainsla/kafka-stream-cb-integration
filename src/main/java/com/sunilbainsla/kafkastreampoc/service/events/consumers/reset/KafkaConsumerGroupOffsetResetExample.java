package com.sunilbainsla.kafkastreampoc.service.events.consumers.reset;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KafkaConsumerGroupOffsetResetExample {

    public static void main(String[] args) {
        // Set the consumer group and topic for offset reset
        String consumerGroup = "kafka-stream-poc-topic3Consumer";
        String topic = "topic3";
        int partition = 0;
        long offset = 2;

        // Generate a unique consumer instance ID
        String consumerInstanceId = UUID.randomUUID().toString();

        // Configure the Kafka Admin REST API endpoint
        String kafkaAdminUrl = "http://localhost:8082";

        // Create the offset reset request payload
        OffsetResetRequest offsetResetRequest = new OffsetResetRequest(Collections.singletonMap(topic, Collections.singletonMap(partition, offset)));

        // Serialize the payload to JSON
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(offsetResetRequest);

        // Create the HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Perform the offset reset request
            String url = kafkaAdminUrl + "/consumers/" + consumerGroup + "/instances/" + consumerInstanceId + "/offsets";
            HttpPost httpPost = new HttpPost(url);

            // Set the request headers
            httpPost.setHeader("Content-Type", "application/vnd.kafka.v2+json");

            // Set the request payload
            httpPost.setEntity(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseJson = EntityUtils.toString(entity);

            // Deserialize the response
            OffsetResetResponse offsetResetResponse = gson.fromJson(responseJson, OffsetResetResponse.class);

            // Print the response
            System.out.println(offsetResetResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class OffsetResetRequest {
        private Map<String, Map<Integer, Long>> offsets;

        public OffsetResetRequest(Map<String, Map<Integer, Long>> offsets) {
            this.offsets = offsets;
        }
    }

    private static class OffsetResetResponse {
        // Add the necessary fields to represent the response structure
        // You can adjust the fields as per the actual response structure from the Kafka Admin API
    }
}
