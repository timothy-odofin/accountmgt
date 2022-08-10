package iobank.org.accountmgt.storage;

import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.model.response.TransactionsResponse;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
@Data
public class LocalStorage {
    private ConcurrentHashMap<String, CustomerResponse> customerStore;
    private ConcurrentHashMap<String, ArrayList<TransactionsResponse>> transactionStore;
    @PostConstruct
    public void init(){
        customerStore= new ConcurrentHashMap<>();
        transactionStore = new ConcurrentHashMap<>();
    }

    public Optional<CustomerResponse> findCustomer(String customerNo){
        if(customerNo==null || !customerStore.contains(customerNo))
            return Optional.empty();
        return Optional.of(customerStore.get(customerNo));
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
            customerStore.put(key, customer);
        }
        return customer;
}

}
