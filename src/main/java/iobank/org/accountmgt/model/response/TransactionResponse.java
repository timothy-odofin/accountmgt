package iobank.org.accountmgt.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
    private ChannelType channel;
    private String narration;
    private String accountNumber;
    private Double balanceBefore;
    private Double balanceAfter;
    private String tranRef;
}
