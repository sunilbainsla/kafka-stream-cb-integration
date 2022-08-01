package com.sunilbainsla.kafkastreampoc.kstream.examples;

class AppConfigs {

    final static String applicationID = "KStreamAggDemo";
    final static String bootstrapServers = "localhost:9092";
    final static String topicName = "employee-processor";
    final static String stateStoreName = "state-store";
    final static String stateStoreLocation = "tmp/state-store";
}
