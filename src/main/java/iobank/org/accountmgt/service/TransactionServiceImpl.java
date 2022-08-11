package iobank.org.accountmgt.service;

import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.Transactions;
import iobank.org.accountmgt.storage.LocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static iobank.org.accountmgt.utils.AppCode.OKAY;
import static iobank.org.accountmgt.utils.MessageUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    private final LocalStorage localStorage;

    @Override
    public ApiResponse withdraw(WithdrawalRequest payload) {

        return null;
    }

    @Override
    public ApiResponse deposit(DepositRequest payload) {
        return null;
    }

    @Override
    public ApiResponse listTransaction(String accountNumber) {
        if(accountNumber==null || accountNumber.isBlank())
            return new ApiResponse(SUCCESS,OKAY,localStorage.listTransaction());
        Optional<Accounts> accountsOptional = localStorage.findAccount(accountNumber);
        if(accountsOptional.isEmpty())
            throw new RecordNotFoundException(ACCOUNT_NOT_FOUND);
        List<Transactions> transactionsList =accountsOptional.get().getTransactions();
        if(transactionsList.isEmpty())
            return new ApiResponse(SUCCESS,OKAY, Collections.emptyList());
        return new ApiResponse(SUCCESS,OKAY, ModelMapper.mapToTransaction(transactionsList));
    }

    @Override
    public ApiResponse listTransactionByDate(String accountNumber, LocalDate tranDate) {
        return null;
    }
}
