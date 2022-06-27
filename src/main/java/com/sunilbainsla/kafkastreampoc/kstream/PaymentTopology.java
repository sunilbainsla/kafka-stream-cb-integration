package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.function.Function;
@Log4j2
public class PaymentTopology implements Function<KStream<String, Payment>, KStream<String, Payment>[]> {
    @Override

        public KStream<String, Payment>[] apply(KStream<String, Payment> stringTopicMessageKStream) {
            KStream<String, Payment> [] paymentValidator=stringTopicMessageKStream.branch(paymentPredicate);
            KStream<String, Payment>  filterStream=stringTopicMessageKStream.filter((k,v)->v.getAmount()>0);
            KStream<String, Payment> [] outputStream=new KStream[]{paymentValidator[0],filterStream};
            return outputStream;
        }

        Predicate<String, Payment> paymentPredicate=(key,message) -> message.getMessage().contains("Sunil");

}
