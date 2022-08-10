package iobank.org.accountmgt.model.response;

import iobank.org.accountmgt.enums.AccountType;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class AccountsResponse {
    private String accountNumber;
    private AccountType accountType;
    private LocalDateTime dateOpened;
    private Double balance;
    private String currency;

}
