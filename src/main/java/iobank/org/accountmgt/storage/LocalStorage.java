package iobank.org.accountmgt.storage;

import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.model.response.TransactionsResponse;
import iobank.org.accountmgt.utils.AppUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
@Data
public class LocalStorage {
    private ConcurrentHashMap<String, CustomerResponse> customerStore;
    private ConcurrentHashMap<String, ArrayList<TransactionsResponse>> transactionStore;
    @Value("${BANK.CODE}")
    private String bankCode;
    @PostConstruct
    public void init(){
        customerStore= new ConcurrentHashMap<>();
        transactionStore = new ConcurrentHashMap<>();
    }

    public Optional<CustomerResponse> findCustomer(String phone){
        if(phone==null || !customerStore.contains(phone))
            return Optional.empty();
        return Optional.of(customerStore.get(phone));
    }
public CustomerResponse save(CustomerResponse customer){
        String key = customer.getPhone();
        if(customerStore.contains(key)){
            CustomerResponse storeCt = customerStore.get(key);
            storeCt.setContactAddress(customer.getContactAddress());
            storeCt.setEmail(customer.getEmail());
            storeCt.setName(customer.getName());
            storeCt.setLastModified(LocalDateTime.now());
            customerStore.put(key, storeCt);
            customer=storeCt;
        }else{
            LinkedHashMap<String, AccountsResponse> accountMap = new LinkedHashMap<>();
            customer.setAccountMap(accountMap);
            customer.setEnrolmentDate(LocalDateTime.now());
            customer.setLastModified(LocalDateTime.now());
            Integer customerId = customerStore.size()+1;
            customer.setId(customerId);
            customer.setCustomerNo(AppUtil.getSerialNumber(customerId,6));
            customerStore.put(key, customer);
        }
        return customer;
}
public boolean isAccountExists(AccountRequest payload){
        CustomerResponse customer = findCustomer(payload.getCustomerPhone()).get();
    LinkedHashMap<String, AccountsResponse> accountMap =customer.getAccountMap();
    for(AccountsResponse rs: accountMap.values()){
        if(payload.getAccountType().equals(rs.getAccountType().label) && payload.getCurrency().equals(rs.getCurrency().label));
            return true;
    }
    return false;
}
public Integer getTotalAccounts(){
   Integer totalAccount=0;
   for(CustomerResponse rs: customerStore.values()){
       totalAccount+= rs.getAccountMap().size();
   }
   totalAccount+=1;
   return totalAccount;
}
public AccountsResponse saveAccount(AccountsResponse payload, String customerPhone){
        if(payload.getAccountNumber()==null || payload.getAccountNumber().isBlank())
            payload.setAccountNumber(AppUtil.generateAccountNo(getTotalAccounts(), bankCode));
        CustomerResponse customer = customerStore.get(customerPhone);
        LinkedHashMap<String, AccountsResponse> accountMap =customer.getAccountMap();
        accountMap.put(payload.getAccountNumber(), payload);
        customer.setAccountMap(accountMap);
        customerStore.put(customerPhone, customer);
        return payload;

}

}
