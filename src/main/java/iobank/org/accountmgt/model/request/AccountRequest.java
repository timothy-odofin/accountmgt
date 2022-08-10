package iobank.org.accountmgt.model.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String customerNo;
    private String accountType;
}
