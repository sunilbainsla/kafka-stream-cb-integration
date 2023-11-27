package com.sunilbainsla.kafkastreampoc.service.events.consumers.batch;


public class RetryItemProcessor<T, S> {//implements ItemProcessor<T, S> {

//    private final ItemProcessor<T, S> delegate;
//    private final Retry myRetry;
//
//    public RetryItemProcessor(ItemProcessor<T, S> delegate, Retry myRetry) {
//        this.delegate = delegate;
//        this.myRetry = myRetry;
//    }
//
//    @Override
//    public S process(T item) throws Exception {
//        return myRetry.executeSupplier(() -> {
//            try {
//                return delegate.process(item);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
}
