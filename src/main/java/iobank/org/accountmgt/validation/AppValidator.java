package iobank.org.accountmgt.validation;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.enums.TransactionType;
import iobank.org.accountmgt.model.request.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static iobank.org.accountmgt.utils.MessageUtil.*;

public class AppValidator {


    public static String isValid(BlockAccountRequest payload){
        ArrayList<String> errorList = new ArrayList<>();
        if(!StringUtils.isNumeric(payload.getAccountNumber()))
            errorList.add(INVALID_CUSTOMER_ACCOUNT_NUMBER);
        if(payload.getEnableAccount()==null)
            errorList.add(INVALID_ACCOUNT_STATUS);
        else if (!validatePhone(payload.getCustomerPhone()))
            errorList.add(INVALID_CONTACT_PHONE);
        return errorList.isEmpty()?"":String.join(",", errorList);
    }
    public static String isValid(WithdrawalRequest payload){
        ArrayList<String> errorList = new ArrayList<>();
        if(!StringUtils.isNumeric(payload.getAccountNumber()))
            errorList.add(INVALID_CUSTOMER_ACCOUNT_NUMBER);
         if(ChannelType.valueOfName(payload.getChannel())==null)
            errorList.add(INVALID_TRANSACTION_CHANNEL);
         if(payload.getAmount()==null ||payload.getAmount()<=0)
             errorList.add(INVALID_TRANSACTION_AMOUNT);
        return errorList.isEmpty()?"":String.join(",", errorList);
    }

    public static String isValid(DepositRequest payload){
        ArrayList<String> errorList = new ArrayList<>();
        if(!StringUtils.isNumeric(payload.getAccountNumber()))
            errorList.add(INVALID_CUSTOMER_ACCOUNT_NUMBER);
        if(ChannelType.valueOfName(payload.getChannel())==null)
            errorList.add(INVALID_TRANSACTION_CHANNEL);
        if(payload.getAmount()==null ||payload.getAmount()<=0)
            errorList.add(INVALID_TRANSACTION_AMOUNT);
        return errorList.isEmpty()?"":String.join(",", errorList);
    }

    public static String isValid(AccountRequest payload){
        ArrayList<String> errorList = new ArrayList<>();
        if(!StringUtils.isNumeric(payload.getCustomerPhone()))
            errorList.add(INVALID_CUSTOMER_NUMBER);
          if(AccountType.valueOfName(payload.getAccountType())==null)
             errorList.add(INVALID_ACCOUNT_TYPE);
         if(CurrencyType.valueOfName(payload.getCurrency())==null)
            errorList.add(INVALID_CURRENCY);
        return errorList.isEmpty()?"":String.join(",", errorList);
    }
    public static String isValid(CustomerRequest payload) {
        ArrayList<String> errorList = new ArrayList<>();
        if (!validateEmail(payload.getEmail()))
            errorList.add(INVALID_EMAIL);
         if (!validatePhone(payload.getPhone()))
            errorList.add(INVALID_CONTACT_PHONE);
         if (!validateData(payload.getContactAddress()))
            errorList.add(INVALID_CONTACT_ADDRESS);
         if (!validateData(payload.getName()))
            errorList.add(INVALID_NAME);
        return errorList.isEmpty()?"":String.join(",", errorList);
    }

    private static boolean validateData(String data) {
        return data != null && !data.isBlank() && data.length() > 3;
    }

    private static boolean validateEmail(String email) {
        String patter = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(patter).matcher(email).matches();
    }

    private static boolean validatePhone(String phone) {
        if (phone == null || phone.isBlank() || !StringUtils.isNumeric(phone)||phone.length() != 11)
            return false;
        return true;
    }
}
