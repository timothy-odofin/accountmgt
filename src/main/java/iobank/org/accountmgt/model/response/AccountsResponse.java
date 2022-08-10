package iobank.org.accountmgt.model.response;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.CurrencyType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountsResponse {
    private String accountNumber;
    private AccountType accountType;
    private LocalDateTime dateOpened;
    private LocalDateTime lastModified;
    private Double balance;
    private CurrencyType currency;
    private Boolean isActive;
    private List<TransactionResponse> transactions;

}
