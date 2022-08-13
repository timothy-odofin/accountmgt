package iobank.org.accountmgt.mapper;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.enums.TransactionType;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    public static Transactions mapToRequest(DepositRequest mapFrom){
        if(mapFrom==null)
            return null;
        return Transactions.builder()
                .accountNumber(mapFrom.getAccountNumber())
                .amount(mapFrom.getAmount())
                .category(TransactionType.DEPOSIT)
                .channel(ChannelType.valueOfName(mapFrom.getChannel()))
                .narration(mapFrom.getNarration())
                .transactionDate(LocalDateTime.now())
                .build();

    }
    public static Transactions mapToRequest(WithdrawalRequest mapFrom){
        if(mapFrom==null)
            return null;
        return Transactions.builder()
                .accountNumber(mapFrom.getAccountNumber())
                .amount(mapFrom.getAmount())
                .category(TransactionType.WITHDRAWAL)
                .channel(ChannelType.valueOfName(mapFrom.getChannel()))
                .narration(mapFrom.getNarration())
                .transactionDate(LocalDateTime.now())
                .build();

    }
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
    public static TransactionResponse mapToTransaction(Transactions mapFrom){
        if(mapFrom==null)
            return null;
        return TransactionResponse.builder()
                .amount(mapFrom.getAmount())
                .category(mapFrom.getCategory())
                .channel(mapFrom.getChannel())
                .transactionDate(mapFrom.getTransactionDate())
                .narration(mapFrom.getNarration())
                .accountNumber(mapFrom.getAccountNumber())
                .balanceAfter(mapFrom.getBalanceAfter())
                .balanceBefore(mapFrom.getBalanceBefore())
                .tranRef(mapFrom.getTranRef())
                .build();
    }
   public static AccountsResponse mapToAccountsResponse(Accounts mapFrom){
        if(mapFrom==null)
            return null;
        return AccountsResponse.builder()
                .balance(mapFrom.getBalance())
                .accountType(mapFrom.getAccountType())
                .currency(mapFrom.getCurrency())
                .isActive(mapFrom.getIsActive())
                .dateOpened(mapFrom.getDateOpened())
                .accountNumber(mapFrom.getAccountNumber())
                .lastModified(mapFrom.getLastModified())
                .build();
    }

    public static List<TransactionResponse> mapToTransaction(List<Transactions> mapFrom){
        return mapFrom.stream()
                .sorted(Comparator.comparing(Transactions::getTransactionDate).reversed())
                .filter(transaction->transaction!=null)
                .map(ModelMapper::mapToTransaction)
                .collect(Collectors.toList());
    }
    public static List<CustomerResponse> mapToCustomerList(List<Customer> mapFrom){
        return mapFrom.stream().filter(customer->customer!=null).map(ModelMapper::mapToCustomer).collect(Collectors.toList());
    }
    public static Customer mapToCustomer(CustomerRequest mapFrom){
        if(mapFrom==null)
            return null;
        Customer mapTo = new Customer();
        mapTo.setName(mapFrom.getName());
        mapTo.setEmail(mapFrom.getEmail());
        mapTo.setPhone(mapFrom.getPhone());
        mapTo.setContactAddress(mapFrom.getContactAddress());
        return mapTo;
    }
    public static Accounts mapToAccount(AccountRequest mapFrom){
        if(mapFrom==null)
            return null;
        Accounts mapTo = new Accounts();
        mapTo.setAccountType(AccountType.valueOfName(mapFrom.getAccountType()));
        mapTo.setBalance(mapFrom.getDeposit());
        mapTo.setLastModified(LocalDateTime.now());
        mapTo.setCurrency(CurrencyType.valueOfName(mapFrom.getCurrency()));
        mapTo.setDateOpened(LocalDateTime.now());
        mapTo.setIsActive(mapFrom.getIsActive());
        return mapTo;
    }
}
