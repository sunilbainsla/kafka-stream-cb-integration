package com.sunilbainsla.kafkastreampoc.service.events.consumers.batch;

//import com.sunilbainsla.kafkastreampoc.kstream.Employee;
//import org.springframework.batch.item.ItemWriter;
//import io.github.resilience4j.retry.Retry;
//
//import java.util.List;

public class RetryItemWriter<Employee> {//implements ItemWriter<Employee> {

//    private final ItemWriter<Employee> delegate;
//    private final Retry myRetry;
//
//    public RetryItemWriter(ItemWriter<Employee> delegate, Retry myRetry) {
//        this.delegate = delegate;
//        this.myRetry = myRetry;
//    }
//
//
//    @Override
//    public void write(List<? extends Employee> list) throws Exception {
//        myRetry.executeRunnable(() -> {
//            try {
//                delegate.write(list);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
}
