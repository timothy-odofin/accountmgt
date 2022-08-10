package iobank.org.accountmgt.service;

import io.swagger.annotations.Api;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.BlockAccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.ApiResponse;

public interface AccountService {
ApiResponse addCustomer(CustomerRequest payload);
ApiResponse addAccountToCustomer(AccountRequest payload);
ApiResponse suspendOrUnsuspendAccount(BlockAccountRequest payload);
ApiResponse listCustomer();
ApiResponse listAllAccounts();
ApiResponse listAccountsByCustomer(String customerPhone);
ApiResponse retrieveAccount(String accountNumber);
}
