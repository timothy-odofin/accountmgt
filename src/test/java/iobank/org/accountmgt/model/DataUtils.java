package iobank.org.accountmgt.model;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.enums.TransactionType;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.utils.AppUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataUtils {
    private static String getAccountNumber(){
        return "1234567898";
    }
    private static Double getTransactionAmount(){
        return 5000.0;
    }
    private static String getCustomerPhone(){
        return "07065990878";
    }
    private static String getContactAddress(){
        return "No2 Rock City Avenue, Goshen Estate Abeokuta, Ogun State";
    }
public static CustomerRequest customerData(){
    return CustomerRequest.builder()
            .contactAddress(getContactAddress())
            .email("odofin@swipe.ng")
            .name("Odofin Timothy")
            .phone(getCustomerPhone())
            .build();
}
    public static DepositRequest depositData(){
        return DepositRequest.builder()
                .accountNumber(getAccountNumber())
                .amount(getTransactionAmount())
                .channel("Cash")
                .narration("Testing deposit")
                .build();
    }

    public static WithdrawalRequest withdrawalData(){
        return WithdrawalRequest.builder()
                .accountNumber(getAccountNumber())
                .amount(getTransactionAmount())
                .channel("Cash")
                .narration("Testing deposit")
                .build();
    }

    public static CustomerResponse getCustomer(){
        return CustomerResponse.builder()
                .customerNo(AppUtil.getSerialNumber(1,6))
                .phone(getCustomerPhone())
                .name("Odofin Oyejide")
                .enrolmentDate(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .email("odofin@swipe.ng")
                .contactAddress(getContactAddress())
                .build();
    }
    public static List<CustomerResponse> getCustomerList(){
        return  Collections.singletonList(getCustomer());
    }

    public static List<TransactionResponse> getTransactions(){

        TransactionResponse data = TransactionResponse.builder()
                .tranRef(AppUtil.getReference())
                .balanceBefore(0.0)
                .balanceAfter(getTransactionAmount())
                .transactionDate(LocalDateTime.now())
                .accountNumber(getAccountNumber())
                .category(TransactionType.DEPOSIT)
                .narration("Deposit")
                .channel(ChannelType.CASH)
                .amount(getTransactionAmount())
                .build();
        return Collections.singletonList(data);

    }

    public static AccountsResponse retrieveAccount(){
    return AccountsResponse.builder()
            .lastModified(LocalDateTime.now())
            .accountType(AccountType.CURRENT)
            .dateOpened(LocalDateTime.now())
            .isActive(true)
            .currency(CurrencyType.NAIRA)
            .balance(getTransactionAmount())
            .accountNumber(getAccountNumber())
            .transactions(getTransactions())
            .build();
    }

}
