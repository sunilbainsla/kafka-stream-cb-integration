package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.apache.kafka.streams.state.Stores;

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


        //Generate ktable
        pushedData(mainPaymentKStream);

        //Read ktable and verify the sortcode
       // verifySortCode(mainPaymentKStream) ;

        KStream<String, Payment>[] outputStream = new KStream[]{isInternalPayment[0],  overseasPayment, refundsPayment, failedPayment,completedPayment};

        return outputStream;
    }

    private void pushedData(KStream<String, Payment> mainPaymentKStream) {
        KTable<String, String> sortCodeTable = mainPaymentKStream
         .filter((k, payment) -> payment.getDebtorAccount().getIdentification() != null) // Filter out payments with no sort code
         .selectKey((k, payment) -> payment.getDebtorAccount().getIdentification()) // Use the sort code as the new key
         .mapValues(payment -> payment.getDebtorAccount().getIdentification()) // Extract sort code as value (String)
         .groupByKey(Grouped.with(Serdes.String(),Serdes.String())) // Group by sort code
         .reduce((aggValue, newValue) -> newValue, Materialized.as("payment-table"));
         sortCodeTable.toStream().to("payment-table-topic", Produced.with(Serdes.String(), Serdes.String()).withKeySerde(Serdes.String()).withValueSerde(Serdes.String()));


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

    private String getIdentification(Payment payment) {
        if (payment.getCurrency().equalsIgnoreCase("GBP")) {
            payment.setMessage(payment.getMessage() + "UK Currency");
        } else {
            payment.setMessage(payment.getMessage() + "NoN UK Currency");
        }
        return payment.getDebtorAccount().getIdentification();
    }


    private void verifySortCode(KStream<String, Payment> mainPaymentKStream) {

        KTable<String, String> sortCodeTable = mainPaymentKStream
         .filter((k, payment) -> payment.getDebtorAccount().getIdentification() != null) // Filter out payments with no sort code
         .selectKey((k, payment) -> payment.getDebtorAccount().getIdentification()) // Use the sort code as the new key
         .mapValues(payment -> payment.getDebtorAccount().getIdentification()) // Extract sort code as value (String)
         .groupByKey(Grouped.with(Serdes.String(),Serdes.String())) // Group by sort code
         .reduce((aggValue, newValue) -> newValue, Materialized.as("payment-table"));

    // Define a ValueJoiner to check the sort code from Payment and the sort code from the KTable
        org.apache.kafka.streams.kstream.ValueJoiner<Payment, String, String> sortCodeChecker = (payment, sortCode) -> {
            // Do the required checks or operations using the payment and the sort code
            // For example, you can compare them, transform the payment, etc.
            if (sortCode != null && sortCode.equals(payment.getDebtorAccount().getIdentification())) {
                return "Sort code match: " + sortCode;
            } else {
                return "Sort code mismatch: " + sortCode;
            }
        };
        KStream<String, String> joinedStream = mainPaymentKStream.leftJoin(
                sortCodeTable,
                sortCodeChecker
        );

        // Print the joined results to the console
        joinedStream.foreach((identification, result) -> System.out.println(identification + " - " + result));

    }
}




