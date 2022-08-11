package iobank.org.accountmgt.route;

import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.service.TransactionService;
import iobank.org.accountmgt.utils.TransactionEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static iobank.org.accountmgt.utils.TransactionEndpoints.*;

@RestController
@RequestMapping(TRANSACTION_ROOT)
@RequiredArgsConstructor
public class TransactionRoute {
    private final TransactionService transactionService;

    @PostMapping(WITHDRAWAL_PATH)
   public ApiResponse withdraw(@RequestBody WithdrawalRequest payload){
       return transactionService.withdraw(payload);
   }
   @PostMapping(DEPOSIT_PATH)
    public  ApiResponse deposit(@RequestBody DepositRequest payload){
        return transactionService.deposit(payload);
    }
    @GetMapping(TransactionEndpoints.LIST_PATH)
    public  ApiResponse<List<TransactionResponse>> listTransaction(@RequestParam(value=ACCOUNT_PARAM, required = false) String accountNumber){
        return transactionService.listTransaction(accountNumber);
    }
    @GetMapping(TRANSACTION_LIST_BY_ACCOUNT)
    public  ApiResponse<List<TransactionResponse>> listTransactionByDate(@RequestParam(value=ACCOUNT_PARAM, required = false) String accountNumber,
                                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(DATE_PARAM) LocalDate tranDate){
     return transactionService.listTransactionByDate(accountNumber, tranDate);
    }
}
