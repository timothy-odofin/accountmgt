package iobank.org.accountmgt.storage;

import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.Customer;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.model.response.Transactions;
import iobank.org.accountmgt.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static iobank.org.accountmgt.utils.MessageUtil.ACCOUNT_NOT_FOUND;
import static iobank.org.accountmgt.utils.MessageUtil.CUSTOMER_NOT_FOUND;

@Service
@ApplicationScope
@Slf4j
public class LocalStorage {
    private LinkedHashMap<String, Customer> customerStore;
    @PostConstruct
    public void init(){
        customerStore=new LinkedHashMap<>();
    }
    @Value("${BANK.CODE}")
    private String bankCode;

    /*
    @param accountNumber
    @Return Array of TransactionResponse
    @Condition verify if an account exists for the given accountNumber
    @Return Empty array of no account exists
    @Return array of TransactionResponse sorted by TransactionDate in descending order
    */
    public synchronized List<TransactionResponse> findTransactionByAccountNumber(String accountNumber){
            Optional<Accounts> accountsOptional = findAccount(accountNumber);
            if(accountsOptional.isPresent()){
               Accounts accounts = accountsOptional.get();
               if(accounts.getTransactions()==null || accounts.getTransactions().isEmpty())
                   return Collections.emptyList();
             return  accounts.getTransactions().stream()
                     .filter(tr->tr!=null && tr.getTransactionDate()!=null)
                     .sorted(Comparator.comparing(Transactions::getTransactionDate).reversed())
                     .map(ModelMapper::mapToTransaction).collect(Collectors.toList());
            }
            return Collections.emptyList();



    }

    public synchronized List<TransactionResponse> getTransactionsByCustomer(Customer customer){
        Map<String,Accounts> maps = customer.getAccountMap();
        List<TransactionResponse> ls = new ArrayList<>();
        if(maps==null || maps.isEmpty())
            return Collections.emptyList();
        maps.values().forEach(rs->ls.addAll(ModelMapper.mapToTransaction(rs.getTransactions())));
        return ls;
    }
    public synchronized List<TransactionResponse> listTransaction(){
       if(customerStore.isEmpty())
           return Collections.emptyList();
       List<TransactionResponse> ls = new ArrayList<>();

       customerStore.forEach((key,customer)->ls.addAll(getTransactionsByCustomer(customer)));

       if(ls.isEmpty())
           return Collections.emptyList();
            return  ls.stream()
                    .sorted(Comparator.comparing(TransactionResponse::getTransactionDate).reversed())
                    .collect(Collectors.toList());
    }
    private Optional<Accounts> findAny(Collection<Accounts> data, String accountNumber){
        return data.stream().filter(account->account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    public synchronized Optional<Accounts> findAccount(String accountNumber){
        if(customerStore.isEmpty())
            return Optional.empty();
        for(Customer ac:customerStore.values()){
            Optional<Accounts> accountsOptional = findAny(ac.getAccountMap().values(),accountNumber);
            if(accountsOptional.isPresent())
                return accountsOptional;
        }
       return Optional.empty();


    }
    public synchronized List<Customer> findAll() {
        if (customerStore.isEmpty())
            return new ArrayList<>();
        ArrayList<Customer> ls = new ArrayList<>();
        ls.addAll(customerStore.values());
        return ls;
    }
    public synchronized Optional<Customer> findCustomerByAccountNumber(String accountNumber) {
        if (accountNumber == null)
            return Optional.empty();
List<Customer> ls = findAll();
return ls.stream()
        .filter(customer->customer !=null && customer.getAccountMap() !=null
                && customer.getAccountMap().containsKey(accountNumber))
        .findFirst();
    }

    public synchronized Optional<Customer> findCustomer(String phone) {
        if (phone == null || (customerStore !=null && !customerStore.containsKey(phone)))
              return Optional.empty();
        return Optional.of(customerStore.get(phone));
    }


    public synchronized Optional<Accounts> findAccountByNumber(String accountNumber, String customerPhone) {
        if(customerStore !=null && !customerStore.containsKey(customerPhone))
            throw new RecordNotFoundException(ACCOUNT_NOT_FOUND);
        Customer customerResponse = customerStore.get(customerPhone);
        LinkedHashMap<String, Accounts> accountsResponseLinkedHashMap = customerResponse.getAccountMap();
        if (accountsResponseLinkedHashMap.containsKey(accountNumber))
            return Optional.of(accountsResponseLinkedHashMap.get(accountNumber));
        return Optional.empty();


    }

    public synchronized Customer save(Customer customer) {
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

    public synchronized boolean isAccountExists(AccountRequest payload) {
        Customer customer = findCustomer(payload.getCustomerPhone()).get();
        LinkedHashMap<String, Accounts> accountMap = customer.getAccountMap();
        for (Accounts rs : accountMap.values()) {
            if (payload.getAccountType().equals(rs.getAccountType().label) && payload.getCurrency().equals(rs.getCurrency().label))
                return true;

        }
        return false;
    }


    public synchronized List<Accounts> findAllAccount() {
        if (customerStore.isEmpty())
            return new ArrayList<>();
        List<Accounts> accountsList = new ArrayList<>();
        customerStore.values().stream().filter(customer -> customer.getAccountMap() != null && !customer.getAccountMap().isEmpty())
                .map(c -> c.getAccountMap().values()).forEach(accountsList::addAll);
        return accountsList;
    }

    public synchronized List<Accounts> listAccount(String customerPhone) {
        Optional<Customer> customerOptional = findCustomer(customerPhone);
        if (customerOptional.isEmpty())
            throw new RecordNotFoundException(CUSTOMER_NOT_FOUND);
        return new LinkedList<>(customerOptional.get().getAccountMap().values());
    }

    public synchronized Integer getTotalAccounts() {
        Integer totalAccount = 0;
        for (Customer rs : customerStore.values()) {
            totalAccount += rs.getAccountMap().size();
        }
        totalAccount += 1;
        return totalAccount;
    }

    public synchronized Accounts saveAccount(Accounts payload, String customerPhone) {
        if (payload.getAccountNumber() == null || payload.getAccountNumber().isBlank())
            payload.setAccountNumber(AppUtil.generateAccountNo(getTotalAccounts(), bankCode));
        Customer customer = customerStore.get(customerPhone);
        LinkedHashMap<String, Accounts> accountMap = customer.getAccountMap()==null?new LinkedHashMap<>():customer.getAccountMap();
        payload.setTransactions(new ArrayList<>());
        accountMap.put(payload.getAccountNumber(), payload);
        customer.setAccountMap(accountMap);
        customerStore.put(customerPhone, customer);
        return payload;

    }

}
