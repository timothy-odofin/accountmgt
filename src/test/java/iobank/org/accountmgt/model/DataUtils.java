package iobank.org.accountmgt.model;

import com.google.gson.internal.LinkedTreeMap;
import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.enums.TransactionType;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.*;
import iobank.org.accountmgt.model.response.*;
import iobank.org.accountmgt.utils.AppUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static iobank.org.accountmgt.utils.AppCode.OKAY;
import static iobank.org.accountmgt.utils.MessageUtil.SUCCESS;
import static iobank.org.accountmgt.utils.MessageUtil.WITHDRAWAL_SUCCESSFUL;
import static iobank.org.accountmgt.utils.TestApiCode.*;
import static iobank.org.accountmgt.utils.TestMessages.*;

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
    public static AccountRequest accountRequest(){
        return AccountRequest.builder()
                .accountType("Savings")
                .currency("NGN")
                .customerPhone(getCustomerPhone())
                .build();
    }

    public static Accounts accounts(){
        return Accounts.builder()
                .accountType(AccountType.SAVINGS)
                .currency(CurrencyType.NAIRA)
                .accountNumber(getAccountNumber())
                .balance(getTransactionAmount())
                .dateOpened(LocalDateTime.now())
                .isActive(true)
                .lastModified(LocalDateTime.now())
                .transactions(new ArrayList<>())
                .lastModified(LocalDateTime.now())
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
                .amount(2000.0)
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

    public static Customer getStoreCustomer(){
        return Customer.builder()
                .customerNo(AppUtil.getSerialNumber(1,6))
                .phone(getCustomerPhone())
                .name("Odofin Oyejide")
                .enrolmentDate(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .email("odofin@swipe.ng")
                .contactAddress(getContactAddress())
                .accountMap(getAccountMap())
                .build();
    }
    public static LinkedHashMap<String, Accounts> getAccountMap(){
        LinkedHashMap<String,Accounts> maps = new LinkedHashMap<>();
        maps.put(getAccountNumber(),accounts());
        return maps;
    }
    public static List<CustomerResponse> getCustomerList(){
        return  Collections.singletonList(getCustomer());
    }
    public static List<AccountsResponse> getAccountList(){
        return Collections.singletonList(ModelMapper.mapToAccountsResponse(accounts()));
    }
    public static String badRequestData(){
        return "Bad request";
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

    public static BlockAccountRequest blockAccount(){
        return BlockAccountRequest.builder()
                .accountNumber(getAccountNumber())
                .enableAccount(false)
                .customerPhone(getCustomerPhone())
                .build();
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
    public static ApiResponse<String> getResult(){
        return new ApiResponse<>(SUCCESS,OKAY, CUSTOMER_CREATED);
    }

    public static ApiResponse<String> getNotFoundResult(){
        return new ApiResponse<>(FAILED,NOT_FOUND, RECORD_NOT_FOUND);
    }
    public static ApiResponse<String> getBadRequestResult(){
        return new ApiResponse<>(FAILED,BAD_REQUEST,BADE_REQUEST );
    }
    public static ApiResponse<String> getDuplicateResult(){
        return new ApiResponse<>(FAILED,DUPLICATE_ID, DUPLICATE_RECORD);
    }

    public static ApiResponse<String> getAccountSuspendedResult(){
        return new ApiResponse<>(SUCCESS,OKAY, ACCOUNT_SUSPENDED_RESP);
    }
    public static ApiResponse<List<CustomerResponse>> getListCustomerResult(){
        return new ApiResponse<>(SUCCESS,OKAY, getCustomerList());
    }

    public static ApiResponse<List<AccountsResponse>> getListAccountResult(){
        return new ApiResponse<>(SUCCESS,OKAY, getAccountList());
    }
    public static ApiResponse<AccountsResponse> retrieveAccountResult(){
        return new ApiResponse<>(SUCCESS,OKAY, retrieveAccount());
    }

    public static ApiResponse<String> retrieveFailedAccountResult(){
        return new ApiResponse<>(FAILED,NOT_FOUND, ACCOUNT_NOT_FOUND);
    } public static ApiResponse<String> getWithdrawalResult(){
        return new  ApiResponse(SUCCESS, OKAY, WITHDRAWAL_SUCCESSFUL);
    }




}
