package iobank.org.accountmgt.service;

import iobank.org.accountmgt.exception.BadRequestException;
import iobank.org.accountmgt.exception.DuplicationRecordException;
import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.BlockAccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.Customer;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.validation.AppValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static iobank.org.accountmgt.utils.AppCode.*;
import static iobank.org.accountmgt.utils.MessageUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements  AccountService{
    private final LocalStorage localStorage;


    @Override
    public ApiResponse addCustomer(CustomerRequest payload) {
        String validationResult = AppValidator.isValid(payload);
        if(!validationResult.isBlank())
            throw new BadRequestException(validationResult);
        Optional<Customer> customerResponseOptional = localStorage.findCustomer(payload.getPhone());
        if(customerResponseOptional.isPresent())
            throw new DuplicationRecordException(DUPLICATE_RECORD);

        Customer customerResponse = ModelMapper.mapToCustomer(payload);
        localStorage.save(customerResponse);
        return new ApiResponse(SUCCESS,CREATED,customerResponse);
    }

    @Override
    public ApiResponse addAccountToCustomer(AccountRequest payload) {
        String validationResult = AppValidator.isValid(payload);
        if(!validationResult.isBlank())
            throw new BadRequestException(validationResult);
        Optional<Customer> customerResponseOptional = localStorage.findCustomer(payload.getCustomerPhone());
        if(customerResponseOptional.isEmpty())
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        if(localStorage.isAccountExists(payload))
            throw new DuplicationRecordException(DUPLICATE_ACCOUNT);
        Accounts accountsResponse =localStorage.saveAccount(ModelMapper.mapToAccount(payload), payload.getCustomerPhone());

        return new ApiResponse(SUCCESS,CREATED,accountsResponse);
    }

    @Override
    public ApiResponse suspendOrUnsuspendAccount(BlockAccountRequest payload) {
        String validationResult = AppValidator.isValid(payload);
        if(!validationResult.isBlank())
            throw new BadRequestException(validationResult);
        Optional<Customer> customerResponseOptional = localStorage.findCustomer(payload.getCustomerPhone());
        if(customerResponseOptional.isEmpty())
            throw new RecordNotFoundException(RECORD_NOT_FOUND);
        Optional<Accounts> accountsResponse= localStorage.findAccountByNumber(payload.getAccountNumber(), payload.getCustomerPhone());
        if(accountsResponse.isEmpty())
            throw new RecordNotFoundException(ACCOUNT_NOT_FOUND);
        Customer customer = customerResponseOptional.get();
        Accounts account = accountsResponse.get();
        account.setIsActive(false);
        account.setLastModified(LocalDateTime.now());

        LinkedHashMap<String, Accounts> accountsResponseLinkedHashMap = customer.getAccountMap();
        accountsResponseLinkedHashMap.put(payload.getAccountNumber(), account);
        customer.setAccountMap(accountsResponseLinkedHashMap);
        localStorage.save(customer);

        return new ApiResponse(SUCCESS,OKAY,PAYMENT_SUCCESSFUL);
    }

    @Override
    public ApiResponse listCustomer() {
        List<CustomerResponse> customerResponseList=ModelMapper.mapToCustomerList( localStorage.findAll());
        return new ApiResponse(SUCCESS,OKAY,customerResponseList);
    }

    @Override
    public ApiResponse listAllAccounts() {
        return null;
    }

    @Override
    public ApiResponse listAccountsByCustomer(String customerPhone) {
        if(customerPhone==null || customerPhone.isBlank())
            return listAllAccounts();
        List<Accounts> accountsList = localStorage.listAccount(customerPhone);
        return new ApiResponse(SUCCESS,OKAY,accountsList);
    }

    @Override
    public ApiResponse retrieveAccount(String accountNumber,String customerPhone) {
        return null;
    }
}
