package iobank.org.accountmgt.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DepositRequest {
    private String accountNumber;
    private Double amount;
    private String channel;
    private String narration;
}
