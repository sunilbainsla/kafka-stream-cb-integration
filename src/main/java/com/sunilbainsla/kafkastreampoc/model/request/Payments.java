package com.sunilbainsla.kafkastreampoc.model.request;

import com.sunilbainsla.kafkastreampoc.model.kafka.Account;
import lombok.Data;

@Data
public class Payments {
    private String message;
    private String id;
    String currency;
    int amount;
    String reference;
    String numericReference;
    String transactionReferenceNumber;
    Account debtorAccount;
    Account creditorAccount;
}
