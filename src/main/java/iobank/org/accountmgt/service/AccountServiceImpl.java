package iobank.org.accountmgt.service;

import iobank.org.accountmgt.exception.BadRequestException;
import iobank.org.accountmgt.exception.DuplicationRecordException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.BlockAccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.validation.AppValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static iobank.org.accountmgt.utils.AppCode.CREATED;
import static iobank.org.accountmgt.utils.AppCode.DUPLICATE;
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
        Optional<CustomerResponse> customerResponseOptional = localStorage.findCustomer(payload.getPhone());
        if(customerResponseOptional.isPresent())
            throw new DuplicationRecordException(DUPLICATE_RECORD);

        CustomerResponse customerResponse = ModelMapper.mapToCustomer(payload);
        localStorage.save(customerResponse);
        return new ApiResponse(SUCCESS,CREATED,customerResponse);
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
