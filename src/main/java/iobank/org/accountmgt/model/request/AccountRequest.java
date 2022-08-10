package iobank.org.accountmgt.model.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String customerPhone;
    private String currency;
    private String accountType;
}
