package iobank.org.accountmgt.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOpened;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModified;
    private Double balance;
    private CurrencyType currency;
    private Boolean isActive;
    private List<TransactionResponse> transactions;

}
