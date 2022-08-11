package iobank.org.accountmgt.model;

import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;

public class DataUtils {
public static CustomerRequest customerData(){
    return CustomerRequest.builder()
            .contactAddress("No2 Rock City Avenue, Goshen Estate Abeokuta, Ogun State")
            .email("odofin@swipe.ng")
            .name("Odofin Timothy")
            .phone("07065990878")
            .build();
}
    public static DepositRequest depositData(){
        return DepositRequest.builder()
                .accountNumber("12344873")
                .amount(2000.0)
                .channel("Cash")
                .narration("Testing deposit")
                .build();
    }

    public static WithdrawalRequest withdrawalData(){
        return WithdrawalRequest.builder()
                .accountNumber("12344873")
                .amount(2000.0)
                .channel("Cash")
                .narration("Testing deposit")
                .build();
    }
}
