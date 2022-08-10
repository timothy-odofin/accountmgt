package iobank.org.accountmgt.storage;

import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.model.response.TransactionsResponse;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
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


}
