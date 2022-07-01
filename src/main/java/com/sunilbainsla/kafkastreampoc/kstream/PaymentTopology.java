package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.function.Function;
@Log4j2

public class PaymentTopology implements Function<KStream<String, Payment>, KStream<String, Payment>[]> {
    PaymentValueTransformer paymentValueTransformer;
    public  PaymentTopology(PaymentValueTransformer paymentValueTransformer)
    {
        this.paymentValueTransformer=paymentValueTransformer;
    }



        public KStream<String, Payment>[] apply(KStream<String, Payment> stringTopicMessageKStream) {

            KStream<String, Payment> [] messageSunil=stringTopicMessageKStream.branch(paymentPredicate);
            KStream<String, Payment>  greaterAmountStream=stringTopicMessageKStream.filter((k,v)->v.getAmount()>0);
             KStream<String, Payment> gbpCurrency=stringTopicMessageKStream.filter((k,v)->v.getCurrency().equalsIgnoreCase("GBP"));
//             KStream<String, Payment> []messageTransform=stringTopicMessageKStream.transformValues(()->paymentValueTransformer).
//                     branch(Named.as("myBranch"),paymentPredicate,paymentPredicate);
////            KStream<String, Payment> numericRef=stringTopicMessageKStream.mapValues(new PaymentValueMapper());
//           KStream<String, Payment> [] outputStream=new KStream[]{messageSunil[0],greaterAmountStream,gbpCurrency,messageTransform,numericRef};
            KStream<String, Payment> [] outputStream=new KStream[]{messageSunil[0],greaterAmountStream,gbpCurrency};


            return outputStream;
        }

        Predicate<String, Payment> paymentPredicate=(key,message) -> message.getMessage().contains("Sunil");

}
