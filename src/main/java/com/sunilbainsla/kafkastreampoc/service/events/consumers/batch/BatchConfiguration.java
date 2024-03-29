package com.sunilbainsla.kafkastreampoc.service.events.consumers.batch;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

//@Configuration
//@EnableBatchProcessing
//@EnableRetry
public class BatchConfiguration {
//    @Autowired
//    public StepBuilderFactory stepBuilderFactory;
//    @Autowired
//    public JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private Step chunkStep;
//
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new ResourcelessTransactionManager();
//    }
//
//    @Bean
//    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
//        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//        factory.setDataSource(dataSource);
//        factory.setTransactionManager(transactionManager);
//        factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
//        factory.setTablePrefix("BATCH_"); // Set a table prefix for batch-related tables
//        return factory.getJobRepository();
//    }
//
//    @Bean
//    public Step chunkStep(
//            ItemReader<Employee> reader,
//            ItemProcessor<Employee, Employee> processor,
//            ItemWriter<Employee> writer,
//            Retry myRetry,
//            CircuitBreaker chunkCircuitBreaker,
//            MyChunkListener chunkListener
//    ) {
//        ItemWriter<Employee> retryWriter = new RetryItemWriter<>(writer,myRetry);
//        ItemProcessor<Employee, Employee> retryProcessor = new RetryItemProcessor<>(processor, myRetry);
//
//        ItemProcessor<Employee, Employee> circuitBreakerProcessor = new CircuitBreakerItemProcessor(chunkCircuitBreaker, retryProcessor);
//        ItemWriter<Employee> circuitBreakerWriter = new CircuitBreakerItemWriter( retryWriter,chunkCircuitBreaker);
//
//        return stepBuilderFactory.get("chunkStep")
//                .<Employee, Employee>chunk(10)
//                .reader(reader)
//                .processor(circuitBreakerProcessor)
//                .writer(circuitBreakerWriter)
//                .faultTolerant()
//                .retryLimit(3)
//                .listener(chunkListener)
//                .retry(Exception.class)
//                .skipLimit(10)
//                .skip(Exception.class)
//                .build();
//    }
//
//    @Bean
//    public Job importUserJob(JobExecutionListener listener) {
//        return jobBuilderFactory.get("importUserJob")
//                .incrementer(new RunIdIncrementer())
//                .listener(listener)
//                .flow(chunkStep) // Reference the chunkStep bean here
//                .end()
//                .build();
//    }
//    @Bean
//    public RetryConfig retryConfig() {
//        return RetryConfig.custom()
//                .maxAttempts(3)
//                .build();
//    }
//
//    @Bean
//    public Retry retry(RetryConfig retryConfig) {
//        return Retry.of("myRetry", retryConfig);
//    }
//
//    @Bean
//    public CircuitBreakerConfig circuitBreakerConfig() {
//        return CircuitBreakerConfig.custom()
//                .failureRateThreshold(50)
//                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
//                .slidingWindowSize(10)
//                .minimumNumberOfCalls(5)
//                .build();
//    }
//
//    @Bean
//    public CircuitBreakerRegistry circuitBreakerRegistry() {
//        return CircuitBreakerRegistry.ofDefaults();
//    }
//
//    @Bean
//    public CircuitBreaker chunkCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
//        return circuitBreakerRegistry.circuitBreaker("chunkCircuitBreaker", circuitBreakerConfig());
//    }
}
