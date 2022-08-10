package iobank.org.accountmgt.model.response;

import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transactions {
    private String accountNumber;
    private TransactionType category;
    private Double amount;
    private LocalDateTime transactionDate;
    private ChannelType channel;
    private String narration;
}
