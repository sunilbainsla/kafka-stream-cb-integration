package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.function.Function;

@Log4j2

public class PaymentTopology implements Function<KStream<String, Payment>, KStream<String, Payment>[]> {
   
    public KStream<String, Payment>[] apply(KStream<String, Payment> mainPaymentKStream) {

        KStream<String, Payment>[] isInternalPayment = mainPaymentKStream.branch(internalPaymentPredicate);
        KStream<String, Payment> overseasPayment = mainPaymentKStream.filter((k, v) -> !v.getCurrency().equalsIgnoreCase("GBP")).
                peek((s, payment) -> System.out.println(s + payment.getMessage()+"Status--->"+payment.getPaymentStatus()));
        KStream<String, Payment> overseasPayments = overseasPayment.map((key, payment) -> new KeyValue<>(key + "Overseas Payment", transformPayment(payment)));

        KStream<String, Payment> refundsPayment = mainPaymentKStream.filter((k, v) -> v.getPaymentStatus().equalsIgnoreCase("REFUNDED"));
        KStream<String, Payment> failedPayment = mainPaymentKStream.filter((k, v) -> v.getPaymentStatus().equalsIgnoreCase("FAILED"));
        KStream<String, Payment> completedPayment = mainPaymentKStream.filter((k, v) -> v.getPaymentStatus().equalsIgnoreCase("COMPLETED"));
        completedPayment=completedPayment.transformValues(() -> new PaymentValueTransformer());
        KStream<String, Payment>[] outputStream = new KStream[]{isInternalPayment[0],  overseasPayment, refundsPayment, failedPayment,completedPayment};

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

    Predicate<String, Payment> internalPaymentPredicate = (key, isInternal) -> isInternal.isInternal();

}
