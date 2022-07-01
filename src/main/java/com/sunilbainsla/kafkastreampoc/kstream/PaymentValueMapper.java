package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import org.apache.kafka.streams.kstream.ValueMapper;

public class PaymentValueMapper implements ValueMapper<Payment,Payment> {

    @Override
    public Payment apply(Payment payment) {
        if (payment.getNumericReference().equalsIgnoreCase("123")) {
            payment.setNumericReference("321");
        }
        return payment;
    }
}
