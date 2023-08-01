package com.sunilbainsla.kafkastreampoc.model.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.stream.Collectors;

public class VargsExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(VargsExample.class);

    public static String generateMDCStringWithRefData() {
        return MDC.getCopyOfContextMap().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("ref_data"))
                .map(entry -> entry.getKey().replace("ref_data_", "") + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

    }

    public static void main(String[] args) {
        MDC.put("ref_data_fileJobId", "123456");
        MDC.put("non_ref_data", "Value 2");
        MDC.put("ref_data_fileName", "eiscdfile.txt");


        String mdcString = generateMDCStringWithRefData();
        if (!mdcString.isEmpty()) {
            LOGGER.info("MDC values with ref_data: {}", mdcString);
        } else {
            LOGGER.info("No MDC values with ref_data found.");
        }
    }
}
