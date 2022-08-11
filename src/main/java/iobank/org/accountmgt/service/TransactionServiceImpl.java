package iobank.org.accountmgt.service;

import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.storage.LocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        return null;
    }

    @Override
    public ApiResponse listTransactionByDate(String accountNumber, LocalDate tranDate) {
        return null;
    }
}
