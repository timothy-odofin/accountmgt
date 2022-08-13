package iobank.org.accountmgt.service;

import iobank.org.accountmgt.model.response.ApiResponse;

import java.util.List;

public interface SettingService {
    ApiResponse<List<String>> listAccountType();
    ApiResponse<List<String>> listChannelType();
    ApiResponse<List<String>> listCurrencyType();
    ApiResponse<List<String>> listTransactionType();
}
