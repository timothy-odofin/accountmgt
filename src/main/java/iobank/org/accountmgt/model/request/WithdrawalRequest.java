package iobank.org.accountmgt.model.request;

import lombok.Data;

@Data
public class WithdrawalRequest {
    private String accountNumber;
    private Double amount;
    private String channel;
    private String narration;
}
