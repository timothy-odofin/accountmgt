package iobank.org.accountmgt.service;

import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.BlockAccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.storage.LocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements  AccountService{
    private final LocalStorage localStorage;

    @Override
    public ApiResponse addCustomer(CustomerRequest payload) {
        return null;
    }

    @Override
    public ApiResponse addAccountToCustomer(AccountRequest payload) {
        return null;
    }

    @Override
    public ApiResponse suspendOrUnsuspendAccount(BlockAccountRequest payload) {
        return null;
    }

    @Override
    public ApiResponse listCustomer() {
        return null;
    }

    @Override
    public ApiResponse listAllAccounts() {
        return null;
    }

    @Override
    public ApiResponse listAccountsByCustomer(String customerPhone) {
        return null;
    }

    @Override
    public ApiResponse retrieveAccount(String accountNumber) {
        return null;
    }
}
