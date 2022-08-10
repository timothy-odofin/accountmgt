package iobank.org.accountmgt.mapper;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.Customer;
import iobank.org.accountmgt.model.response.CustomerResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {
    public static CustomerResponse mapToCustomer(Customer mapFrom){
        if(mapFrom==null)
            return null;
     return CustomerResponse.builder()
                .contactAddress(mapFrom.getContactAddress())
                .customerNo(mapFrom.getCustomerNo())
                .email(mapFrom.getEmail())
                .enrolmentDate(mapFrom.getEnrolmentDate())
                .lastModified(mapFrom.getLastModified())
                .name(mapFrom.getName())
                .phone(mapFrom.getPhone())
                .build();

    }
    public static List<CustomerResponse> mapToCustomerList(List<Customer> mapFrom){
        return mapFrom.stream().filter(customer->customer!=null).map(customer -> mapToCustomer(customer)).collect(Collectors.toList());
    }
    public static Customer mapToCustomer(CustomerRequest mapFrom){
        if(mapFrom==null)
            return null;
        Customer mapTo = new Customer();
        mapTo.setName(mapFrom.getName());
        mapTo.setEmail(mapFrom.getEmail());
        mapTo.setPhone(mapTo.getPhone());
        mapTo.setContactAddress(mapTo.getContactAddress());
        return mapTo;
    }
    public static Accounts mapToAccount(AccountRequest mapFrom){
        if(mapFrom==null)
            return null;
        Accounts mapTo = new Accounts();
        mapTo.setAccountType(AccountType.valueOfName(mapFrom.getAccountType()));
        mapTo.setBalance(0.0);
        mapTo.setLastModified(LocalDateTime.now());
        mapTo.setCurrency(CurrencyType.valueOfName(mapFrom.getCurrency()));
        mapTo.setDateOpened(LocalDateTime.now());
        mapTo.setIsActive(true);
        return mapTo;
    }
}
