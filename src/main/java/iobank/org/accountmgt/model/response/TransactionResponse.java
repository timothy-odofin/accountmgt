package iobank.org.accountmgt.model.response;

import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransactionResponse {
    private TransactionType category;
    private Double amount;
    private LocalDateTime transactionDate;
    private ChannelType channel;
    private String narration;
    private String accountNumber;
}
