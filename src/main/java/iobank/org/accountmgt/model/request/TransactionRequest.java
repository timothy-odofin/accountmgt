package iobank.org.accountmgt.model.request;

import lombok.Data;

@Data
public class TransactionRequest {
    private String accountNumber;
    private String transactionType;
    private Double amount;
    private String channel;
    private String narration;
}
