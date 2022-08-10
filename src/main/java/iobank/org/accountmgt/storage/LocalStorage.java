package iobank.org.accountmgt.storage;

import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.Customer;
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

import static iobank.org.accountmgt.utils.MessageUtil.CUSTOMER_NOT_FOUND;

@Component
@ApplicationScope
@Data
public class LocalStorage {
    private ConcurrentHashMap<String, Customer> customerStore;
    private ConcurrentHashMap<String, ArrayList<TransactionsResponse>> transactionStore;
    @Value("${BANK.CODE}")
    private String bankCode;
    @PostConstruct
    public void init(){
        customerStore= new ConcurrentHashMap<>();
        transactionStore = new ConcurrentHashMap<>();
    }
public List<Customer> findAll(){
      if(customerStore.isEmpty())
          return new ArrayList<>();
      return new LinkedList<>(customerStore.values());
}
    public Optional<Customer> findCustomer(String phone){
        if(phone==null || !customerStore.contains(phone))
            return Optional.empty();
        return Optional.of(customerStore.get(phone));
    }
    public Optional<Accounts> findAccountByNumber(String accountNumber, String customerPhone){
       Customer customerResponse = customerStore.get(customerPhone);
       LinkedHashMap<String, Accounts> accountsResponseLinkedHashMap =customerResponse.getAccountMap();
       if(accountsResponseLinkedHashMap.containsKey(accountNumber))
           return Optional.of(accountsResponseLinkedHashMap.get(accountNumber));
       return Optional.empty();


    }
public Customer save(Customer customer){
        String key = customer.getPhone();
        if(customerStore.contains(key)){
            Customer storeCt = customerStore.get(key);
            storeCt.setContactAddress(customer.getContactAddress());
            storeCt.setEmail(customer.getEmail());
            storeCt.setName(customer.getName());
            storeCt.setLastModified(LocalDateTime.now());
            customerStore.put(key, storeCt);
            customer=storeCt;
        }else{
            LinkedHashMap<String, Accounts> accountMap = new LinkedHashMap<>();
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
        Customer customer = findCustomer(payload.getCustomerPhone()).get();
    LinkedHashMap<String, Accounts> accountMap =customer.getAccountMap();
    for(Accounts rs: accountMap.values()){
        if(payload.getAccountType().equals(rs.getAccountType().label) && payload.getCurrency().equals(rs.getCurrency().label));
            return true;
    }
    return false;
}
public List<Accounts> listAccount(String customerPhone){
        Optional<Customer> customerOptional = findCustomer(customerPhone);
        if(customerOptional.isEmpty())
            throw new RecordNotFoundException(CUSTOMER_NOT_FOUND);
        return new LinkedList<>(customerOptional.get().getAccountMap().values());
}
public Integer getTotalAccounts(){
   Integer totalAccount=0;
   for(Customer rs: customerStore.values()){
       totalAccount+= rs.getAccountMap().size();
   }
   totalAccount+=1;
   return totalAccount;
}
public Accounts saveAccount(Accounts payload, String customerPhone){
        if(payload.getAccountNumber()==null || payload.getAccountNumber().isBlank())
            payload.setAccountNumber(AppUtil.generateAccountNo(getTotalAccounts(), bankCode));
        Customer customer = customerStore.get(customerPhone);
        LinkedHashMap<String, Accounts> accountMap =customer.getAccountMap();
        accountMap.put(payload.getAccountNumber(), payload);
        customer.setAccountMap(accountMap);
        customerStore.put(customerPhone, customer);
        return payload;

}

}
