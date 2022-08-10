package iobank.org.accountmgt.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CustomerResponse {
    private String customerNo;
    private String name;
    private String phone;
    private String email;
    private String contactAddress;
    private LocalDateTime enrolmentDate;
    private LocalDateTime lastModified;
}
