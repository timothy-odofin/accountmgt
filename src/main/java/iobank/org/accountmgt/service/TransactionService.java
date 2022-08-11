package iobank.org.accountmgt.service;

import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    ApiResponse withdraw(WithdrawalRequest payload);
    ApiResponse deposit(DepositRequest payload);
    ApiResponse<List<TransactionResponse>> listTransaction(String accountNumber);
    ApiResponse<List<TransactionResponse>> listTransactionByDate(String accountNumber, LocalDate tranDate);
}
