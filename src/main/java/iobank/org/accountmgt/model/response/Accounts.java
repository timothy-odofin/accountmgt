package iobank.org.accountmgt.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.CurrencyType;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@ToString
public class Accounts {
    private String accountNumber;
    private AccountType accountType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOpened;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModified;
    private Double balance;
    private CurrencyType currency;
    private Boolean isActive;
    private ArrayList<Transactions> transactions;

}
