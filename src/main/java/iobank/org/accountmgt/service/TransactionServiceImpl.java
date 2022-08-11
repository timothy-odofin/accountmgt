package iobank.org.accountmgt.service;

import iobank.org.accountmgt.exception.BadRequestException;
import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.*;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.validation.AppValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static iobank.org.accountmgt.utils.AppCode.OKAY;
import static iobank.org.accountmgt.utils.MessageUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final LocalStorage localStorage;

    @Override
    public ApiResponse withdraw(WithdrawalRequest payload) {

        return null;
    }

    @Override
    public ApiResponse deposit(DepositRequest payload) {
        String errorResult = AppValidator.isValid(payload);
        if (!errorResult.isBlank())
            throw new BadRequestException(errorResult);
        Optional<Customer> customerOptional = localStorage.findCustomerByAccountNumber(payload.getAccountNumber());
        if (customerOptional.isEmpty())
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        Customer customer = customerOptional.get();
        LinkedHashMap<String, Accounts> accountsLinkedHashMap = customer.getAccountMap();

        Accounts accounts = accountsLinkedHashMap.get(payload.getAccountNumber());
        accounts.setBalance(accounts.getBalance() + payload.getAmount());
        ArrayList<Transactions> transactions = accounts.getTransactions() == null ? new ArrayList<>() : accounts.getTransactions();
        Transactions transaction = ModelMapper.mapToRequest(payload);
        transactions.add(transaction);
        accounts.setTransactions(transactions);
        accountsLinkedHashMap.put(payload.getAccountNumber(), accounts);
        customer.setAccountMap(accountsLinkedHashMap);
        localStorage.save(customer);

        return new ApiResponse(SUCCESS, OKAY, PAYMENT_SUCCESSFUL);
    }

    @Override
    public ApiResponse<List<TransactionResponse>> listTransaction(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank())
            return new ApiResponse(SUCCESS, OKAY, localStorage.listTransaction());
        Optional<Accounts> accountsOptional = localStorage.findAccount(accountNumber);
        if (accountsOptional.isEmpty())
            throw new RecordNotFoundException(ACCOUNT_NOT_FOUND);
        List<Transactions> transactionsList = accountsOptional.get().getTransactions();
        if (transactionsList.isEmpty())
            return new ApiResponse(SUCCESS, OKAY, Collections.emptyList());
        return new ApiResponse(SUCCESS, OKAY, ModelMapper.mapToTransaction(transactionsList));
    }

    @Override
    public ApiResponse<List<TransactionResponse>> listTransactionByDate(String accountNumber, LocalDate tranDate) {
        ApiResponse<List<TransactionResponse>> response = listTransaction(accountNumber);
        List<TransactionResponse> dataList = response.getData();
        if (dataList.isEmpty())
            return response;
        List<TransactionResponse> ls = dataList.stream()
                .filter(rs -> rs.getTransactionDate().toLocalDate().equals(tranDate))
                .sorted(Comparator.comparing(TransactionResponse::getTransactionDate).reversed())
                .collect(Collectors.toList());
        return new ApiResponse<>(SUCCESS, OKAY, ls);
    }
}
