package iobank.org.accountmgt.model.response;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@Data
@ToString
public class CustomerResponse {
    private Integer id;
    private String customerNo;
    private String name;
    private String phone;
    private String email;
    private String contactAddress;
    private LocalDateTime enrolmentDate;
    private LocalDateTime lastModified;
    LinkedHashMap<String, AccountsResponse> accountMap;

}
