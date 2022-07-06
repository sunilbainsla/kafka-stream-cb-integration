package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.function.Function;

@Log4j2

public class ArbitraryTopology implements Function<KStream<String, Payment>, KStream<String, Payment>[]> {

    public KStream<String, Payment>[] apply(KStream<String, Payment> mainPaymentKStream) {

        KStream<String, Payment>[] messageSunil = mainPaymentKStream.branch(paymentPredicate);
    
        KStream<String, Payment>[] outputStream = new KStream[]{messageSunil[0]};

        return outputStream;
    }

    private Payment transformPayment(Payment payment) {
        if (payment.getCurrency().equalsIgnoreCase("GBP")) {
            payment.setMessage(payment.getMessage() + "UK Currency");
        } else {
            payment.setMessage(payment.getMessage() + "NoN UK Currency");
        }
        return payment;
    }

    Predicate<String, Payment> paymentPredicate = (key, message) -> message.getMessage().contains("Sunil");

}
