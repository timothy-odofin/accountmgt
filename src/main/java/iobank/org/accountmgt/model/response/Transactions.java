package iobank.org.accountmgt.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transactions {
    private String accountNumber;
    private TransactionType category;
    private Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
    private ChannelType channel;
    private String narration;
}
