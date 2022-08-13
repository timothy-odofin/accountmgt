package iobank.org.accountmgt.service;

import iobank.org.accountmgt.enums.AccountType;
import iobank.org.accountmgt.enums.ChannelType;
import iobank.org.accountmgt.enums.CurrencyType;
import iobank.org.accountmgt.enums.TransactionType;
import iobank.org.accountmgt.model.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static iobank.org.accountmgt.utils.AppCode.OKAY;
import static iobank.org.accountmgt.utils.MessageUtil.SUCCESS;

@Service
public class SettingServiceImpl implements SettingService{
    @Override
    public ApiResponse<List<String>> listAccountType() {
        return new ApiResponse<>(SUCCESS,OKAY, AccountType.list());
    }

    @Override
    public ApiResponse<List<String>> listChannelType() {
        return new ApiResponse<>(SUCCESS,OKAY, ChannelType.list());
    }

    @Override
    public ApiResponse<List<String>> listCurrencyType() {
        return new ApiResponse<>(SUCCESS,OKAY, CurrencyType.list());
    }

    @Override
    public ApiResponse<List<String>> listTransactionType() {
        return new ApiResponse<>(SUCCESS,OKAY, TransactionType.list());
    }
}
