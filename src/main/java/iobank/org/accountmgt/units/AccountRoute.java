package iobank.org.accountmgt.units;

import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.BlockAccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static iobank.org.accountmgt.utils.AccountEndpoints.*;

@RestController
@RequestMapping(CUSTOMER_ROOT)
@RequiredArgsConstructor
public class AccountRoute {
    private final AccountService accountService;

    @PostMapping(ADD_PATH)
    ApiResponse addCustomer(@RequestBody CustomerRequest payload){
        return accountService.addCustomer(payload);
    }
    @PostMapping(ADD_ACCOUNT_PATH)
    ApiResponse addAccountToCustomer(@RequestBody AccountRequest payload){
        return accountService.addAccountToCustomer(payload);
    }
    @PostMapping(SUSPEND_ACCOUNT_PATH)
    ApiResponse suspendOrUnsuspendAccount(@RequestBody BlockAccountRequest payload){
        return accountService.suspendOrUnsuspendAccount(payload);
    }
    @GetMapping(LIST_PATH)
    ApiResponse listCustomer(){
        return accountService.listCustomer();
    }
    @GetMapping(LIST_ACCOUNT_PATH)
    ApiResponse listAccountsByCustomer(@RequestParam(value=PHONE_PARAM, required = false) String customerPhone){
        return accountService.listAccountsByCustomer(customerPhone);
    }
    @GetMapping(ACCOUNT_PATH)
    ApiResponse retrieveAccount(@RequestParam(ACCOUNT_PARAM) String accountNumber,@RequestParam(PHONE_PARAM) String customerPhone){
        return accountService.retrieveAccount(accountNumber, customerPhone);
    }
    @GetMapping(ACCOUNT_PATH_GET)
    ApiResponse retrieveAccount(@RequestParam(ACCOUNT_PARAM)String accountNumber){
        return accountService.retrieveAccount(accountNumber);
    }
}
