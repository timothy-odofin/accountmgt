package iobank.org.accountmgt.service;

import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.ApiResponse;

import java.time.LocalDate;

public interface TransactionService {
    ApiResponse withdraw(WithdrawalRequest payload);
    ApiResponse deposit(DepositRequest payload);
    ApiResponse listTransaction(String accountNumber);
    ApiResponse listTransactionByDate(String accountNumber, LocalDate tranDate);
}
