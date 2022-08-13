package iobank.org.accountmgt.controller;

import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static iobank.org.accountmgt.utils.SettingEndpoints.*;

@RestController
@RequestMapping(SETTING_ROOT)
@RequiredArgsConstructor
public class SettingController {
    private final SettingService settingService;

    @GetMapping(ACCOUNT)
    ApiResponse<List<String>> listAccountType(){
        return settingService.listAccountType();
    }
    @GetMapping(CHANNEL)
    ApiResponse<List<String>> listChannelType(){
        return settingService.listChannelType();
    }
    @GetMapping(CURRENCY)
    ApiResponse<List<String>> listCurrencyType(){
        return settingService.listCurrencyType();
    }
    @GetMapping(TRANSACTION)
    ApiResponse<List<String>> listTransactionType(){
        return settingService.listTransactionType();
    }

}
