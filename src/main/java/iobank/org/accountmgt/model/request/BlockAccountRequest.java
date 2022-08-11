package iobank.org.accountmgt.model.request;

import lombok.Data;

@Data
public class BlockAccountRequest {
    private String customerPhone;
    private String accountNumber;
    private Boolean enableAccount;
}
