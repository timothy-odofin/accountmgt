package iobank.org.accountmgt.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Integer id;
    private String customerNo;
    private String name;
    private String phone;
    private String email;
    private String contactAddress;
    private LocalDateTime enrolmentDate;
    private LocalDateTime lastModified;
    LinkedHashMap<String, Accounts> accountMap;

}
