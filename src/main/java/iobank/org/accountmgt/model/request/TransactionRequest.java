package iobank.org.accountmgt.model.request;

import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private String accountNumber;
    private TransactionType category;
    private Double amount;
    private ChannelType channel;
    private String narration;
}
