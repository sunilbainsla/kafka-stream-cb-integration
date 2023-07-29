package com.sunilbainsla.kafkastreampoc.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class Payment {
    String paymentId;
    private String message;
    String currency;
    int amount;
    String reference;
    String numericReference;
    String transactionReferenceNumber;
    Account debtorAccount;
    Account creditorAccount;
    boolean isInternal;
    PaymentStatus paymentStatus;
    String description;
}
