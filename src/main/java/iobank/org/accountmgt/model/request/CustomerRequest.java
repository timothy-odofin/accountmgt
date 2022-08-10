package iobank.org.accountmgt.model.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerRequest {
    private String name;
    private String phone;
    private String email;
    private String contactAddress;
}
