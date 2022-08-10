package iobank.org.accountmgt.model.response;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.CurrencyType;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class Accounts {
    private String accountNumber;
    private AccountType accountType;
    private LocalDateTime dateOpened;
    private LocalDateTime lastModified;
    private Double balance;
    private CurrencyType currency;
    private Boolean isActive;

}
