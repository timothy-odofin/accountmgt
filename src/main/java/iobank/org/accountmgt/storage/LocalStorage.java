package iobank.org.accountmgt.storage;

import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.Customer;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.model.response.Transactions;
import iobank.org.accountmgt.utils.AppUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static iobank.org.accountmgt.utils.MessageUtil.ACCOUNT_NOT_FOUND;
import static iobank.org.accountmgt.utils.MessageUtil.CUSTOMER_NOT_FOUND;

@Service
@ApplicationScope
public class LocalStorage {
    private LinkedHashMap<String, Customer> customerStore;
    private LinkedHashMap<String, ArrayList<Transactions>> transactionStore;
    @PostConstruct
    public void init(){
        customerStore=new LinkedHashMap<>();
        transactionStore= new LinkedHashMap<>();

    }
    @Value("${BANK.CODE}")
    private String bankCode;

    public List<TransactionResponse> findTransactionByAccountNumber(String accountNumber){
        if(transactionStore.isEmpty())
            return new ArrayList<>();
       return transactionStore.get(accountNumber).stream()
               .filter(rs->rs !=null && rs.getAccountNumber()!=null)
               .map(ModelMapper::mapToTransaction)
               .collect(Collectors.toList());


    }
    public List<Customer> findAll() {
        if (customerStore.isEmpty())
            return new ArrayList<>();
        ArrayList<Customer> ls = new ArrayList<>();
        ls.addAll(customerStore.values());
        System.out.println("Size.................."+ ls.size());
        return ls;
    }

    public Optional<Customer> findCustomer(String phone) {
        if (phone == null || (customerStore !=null && !customerStore.containsKey(phone)))
            return Optional.empty();
        return Optional.of(customerStore.get(phone));
    }

    public Optional<Accounts> findAccountByNumber(String accountNumber, String customerPhone) {
        if(customerStore !=null && !customerStore.containsKey(customerPhone))
            throw new RecordNotFoundException(ACCOUNT_NOT_FOUND);
        Customer customerResponse = customerStore.get(customerPhone);
        LinkedHashMap<String, Accounts> accountsResponseLinkedHashMap = customerResponse.getAccountMap();
        if (accountsResponseLinkedHashMap.containsKey(accountNumber))
            return Optional.of(accountsResponseLinkedHashMap.get(accountNumber));
        return Optional.empty();


    }

    public Customer save(Customer customer) {
        String key = customer.getPhone();
        if (customerStore.containsKey(key)) {
            Customer storeCt = customerStore.get(key);
            storeCt.setContactAddress(customer.getContactAddress());
            storeCt.setEmail(customer.getEmail());
            storeCt.setName(customer.getName());
            storeCt.setLastModified(LocalDateTime.now());
            customerStore.put(key, storeCt);
            customer = storeCt;
        } else {
            LinkedHashMap<String, Accounts> accountMap = new LinkedHashMap<>();
            customer.setAccountMap(accountMap);
            customer.setEnrolmentDate(LocalDateTime.now());
            customer.setLastModified(LocalDateTime.now());
            Integer customerId = customerStore.size() + 1;
            customer.setId(customerId);
            customer.setCustomerNo(AppUtil.getSerialNumber(customerId, 6));
            customerStore.put(key, customer);
        }
        return customer;
    }

    public boolean isAccountExists(AccountRequest payload) {
        Customer customer = findCustomer(payload.getCustomerPhone()).get();
        LinkedHashMap<String, Accounts> accountMap = customer.getAccountMap();
        for (Accounts rs : accountMap.values()) {
            if (payload.getAccountType().equals(rs.getAccountType().label) && payload.getCurrency().equals(rs.getCurrency().label))
                return true;

        }
        return false;
    }


    public List<Accounts> findAllAccount() {
        if (customerStore.isEmpty())
            return new ArrayList<>();
        List<Accounts> accountsList = new ArrayList<>();
        customerStore.values().stream().filter(customer -> customer.getAccountMap() != null && !customer.getAccountMap().isEmpty())
                .map(c -> c.getAccountMap().values()).forEach(accountsList::addAll);
        return accountsList;
    }

    public List<Accounts> listAccount(String customerPhone) {
        Optional<Customer> customerOptional = findCustomer(customerPhone);
        if (customerOptional.isEmpty())
            throw new RecordNotFoundException(CUSTOMER_NOT_FOUND);
        return new LinkedList<>(customerOptional.get().getAccountMap().values());
    }

    public Integer getTotalAccounts() {
        Integer totalAccount = 0;
        for (Customer rs : customerStore.values()) {
            totalAccount += rs.getAccountMap().size();
        }
        totalAccount += 1;
        return totalAccount;
    }

    public Accounts saveAccount(Accounts payload, String customerPhone) {
        if (payload.getAccountNumber() == null || payload.getAccountNumber().isBlank())
            payload.setAccountNumber(AppUtil.generateAccountNo(getTotalAccounts(), bankCode));
        Customer customer = customerStore.get(customerPhone);
        LinkedHashMap<String, Accounts> accountMap = customer.getAccountMap();
        accountMap.put(payload.getAccountNumber(), payload);
        customer.setAccountMap(accountMap);
        customerStore.put(customerPhone, customer);
        return payload;

    }

}
