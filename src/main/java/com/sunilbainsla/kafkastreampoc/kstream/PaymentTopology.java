package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.function.Function;

@Log4j2

public class PaymentTopology implements Function<KStream<String, Payment>, KStream<String, Payment>[]> {
    PaymentValueTransformer paymentValueTransformer;

    public PaymentTopology(PaymentValueTransformer paymentValueTransformer) {
        this.paymentValueTransformer = paymentValueTransformer;
    }

    public KStream<String, Payment>[] apply(KStream<String, Payment> mainPaymentKStream) {

        KStream<String, Payment>[] messageSunil = mainPaymentKStream.branch(paymentPredicate);
        KStream<String, Payment> greaterAmountStream = mainPaymentKStream.filter((k, v) -> v.getAmount() > 0);
        KStream<String, Payment> gbpCurrency = mainPaymentKStream.filter((k, v) -> v.getCurrency().equalsIgnoreCase("GBP")).
                peek((s, payment) -> System.out.println(s + payment.getMessage()));
        KStream<String, Payment> mapTransform = mainPaymentKStream.map((key, payment) -> new KeyValue<>(key + "Sunil", transformPayment(payment)));

        KStream<String, Payment> transformValues = mainPaymentKStream.transformValues(() -> new PaymentValueTransformer());

        KStream<String, Payment>[] outputStream = new KStream[]{messageSunil[0], greaterAmountStream, gbpCurrency, mapTransform, transformValues};

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
