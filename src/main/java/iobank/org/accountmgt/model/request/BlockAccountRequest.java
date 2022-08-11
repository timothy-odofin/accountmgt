package iobank.org.accountmgt.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BlockAccountRequest {
    private String customerPhone;
    private String accountNumber;
    private Boolean enableAccount;
}
