package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public class PaymentValueTransformer implements ValueTransformer< Payment,Payment> {

    @Override
    public void init(ProcessorContext processorContext) {


    }

    @Override
    public Payment transform(Payment payment) {
        if(payment.getCurrency().equalsIgnoreCase("GBP"))
        {
            payment.setMessage(payment.getMessage()+"UK Currency");
        }
        else{
            payment.setMessage(payment.getMessage()+"NoN UK Currency");
        }
        return payment;
    }

    @Override
    public void close() {

    }
}
