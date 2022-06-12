package com.sunilbainsla.kafkastreampoc.kstream;

import com.sunilbainsla.kafkastreampoc.model.kafka.TopicMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.function.Function;
@Log4j2
public class PaymentTopology implements Function<KStream<String, TopicMessage>, KStream<String, TopicMessage>[]> {
    @Override
    public KStream<String, TopicMessage>[] apply(KStream<String, TopicMessage> stringTopicMessageKStream) {
        KStream<String, TopicMessage> [] paymentValidator=stringTopicMessageKStream.branch(new Predicate<String, TopicMessage>() {
            @Override
            public boolean test(String s, TopicMessage topicMessage) {
                return true;
            }
        });
        KStream<String, TopicMessage> [] outputStream=new KStream[]{paymentValidator[0],paymentValidator[0]};
        return outputStream;
    }
}
