package iobank.org.accountmgt.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WithdrawalRequest {
    private String accountNumber;
    private Double amount;
    private String channel;
    private String narration;
}
