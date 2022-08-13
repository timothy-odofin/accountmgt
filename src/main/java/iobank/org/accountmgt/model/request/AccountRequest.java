package iobank.org.accountmgt.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountRequest {
    private String customerPhone;
    private String currency;
    private String accountType;
    private Double deposit;
    private Boolean isActive;
}
