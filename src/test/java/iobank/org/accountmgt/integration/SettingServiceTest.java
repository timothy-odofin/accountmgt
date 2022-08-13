package iobank.org.accountmgt.integration;

import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.Customer;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.SettingEndpoints.*;
import static iobank.org.accountmgt.utils.TestApiCode.OKAY;
import static iobank.org.accountmgt.utils.TestMessages.SUCCESS;
import static iobank.org.accountmgt.utils.TransactionEndpoints.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class SettingServiceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private SettingService settingService;

    @Test
    void test_listAccountType() throws Exception {

        MvcResult mvcResult = mvc.perform(get(SETTING_ROOT + ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<String>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
       List<String> ls = settingService.listAccountType().getData();
       assertEquals(result.getData(), ls);

    }

    @Test
    void test_listChannelType() throws Exception {

        MvcResult mvcResult = mvc.perform(get(SETTING_ROOT + CHANNEL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<String>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        List<String> ls = settingService.listChannelType().getData();
        assertEquals(result.getData(), ls);

    }
    @Test
    void test_listCurrencyType() throws Exception {

        MvcResult mvcResult = mvc.perform(get(SETTING_ROOT + CURRENCY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<String>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        List<String> ls = settingService.listCurrencyType().getData();
        assertEquals(result.getData(), ls);

    }
    @Test
    void test_listTransactionType() throws Exception {

        MvcResult mvcResult = mvc.perform(get(SETTING_ROOT + TRANSACTION)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<String>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        List<String> ls = settingService.listTransactionType().getData();
        assertEquals(result.getData(), ls);

    }
}
