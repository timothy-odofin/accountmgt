package iobank.org.accountmgt.mapper;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;

import java.time.LocalDateTime;

public class ModelMapper {
    public static CustomerResponse mapToCustomer(CustomerRequest mapFrom){
        if(mapFrom==null)
            return null;
        CustomerResponse mapTo = new CustomerResponse();
        mapTo.setName(mapFrom.getName());
        mapTo.setEmail(mapFrom.getEmail());
        mapTo.setPhone(mapTo.getPhone());
        mapTo.setContactAddress(mapTo.getContactAddress());
        return mapTo;
    }
    public static AccountsResponse mapToAccount(AccountRequest mapFrom){
        if(mapFrom==null)
            return null;
        AccountsResponse mapTo = new AccountsResponse();
        mapTo.setAccountType(AccountType.valueOfName(mapFrom.getAccountType()));
        mapTo.setBalance(0.0);
        mapTo.setCurrency(CurrencyType.valueOfName(mapFrom.getCurrency()));
        mapTo.setDateOpened(LocalDateTime.now());
        return mapTo;
    }
}
