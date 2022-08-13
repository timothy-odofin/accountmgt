package iobank.org.accountmgt.units;

import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.service.TransactionService;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.validation.AppValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.AccountEndpoints.CUSTOMER_ROOT;
import static iobank.org.accountmgt.utils.AccountEndpoints.LIST_ACCOUNT_PATH;
import static iobank.org.accountmgt.utils.MessageUtil.PAYMENT_SUCCESSFUL;
import static iobank.org.accountmgt.utils.TestApiCode.*;
import static iobank.org.accountmgt.utils.TestMessages.FAILED;
import static iobank.org.accountmgt.utils.TestMessages.SUCCESS;
import static iobank.org.accountmgt.utils.TransactionEndpoints.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionRoute.class)
@Slf4j
class TransactionRouteTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private LocalStorage localStorage;
    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void test_withdraw_success() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.withdrawalData())).thenReturn("");
        }
        when(localStorage.findCustomerByAccountNumber(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(transactionService.withdraw(any())).thenReturn(DataUtils.getWithdrawalResult());
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT+WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.withdrawalData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
    }
    @Test
    void test_withdraw_fail_account_not_active() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.withdrawalData())).thenReturn("");
        }
        when(localStorage.findCustomerByAccountNumber(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(transactionService.withdraw(any())).thenReturn(DataUtils.getWithdrawalAccountNotActiveResult());
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT+WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.withdrawalData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
    }

    @Test
    void test_withdraw_bad_request() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.withdrawalInvalidData())).thenReturn("Bad request");
        }
        when(localStorage.findCustomerByAccountNumber(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(transactionService.withdraw(any())).thenReturn(DataUtils.getWithdrawalAccountNotActiveResult());
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT+WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.withdrawalData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
    }
    @Test
    void test_deposit_success() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.depositData())).thenReturn("");
        }
        when(localStorage.findCustomerByAccountNumber(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(transactionService.deposit(any())).thenReturn(DataUtils.getDepositResult());
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT+DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.depositData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData(),PAYMENT_SUCCESSFUL);
    }

    @Test
    void test_deposit_fail() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.depositData())).thenReturn("");
        }
        when(localStorage.findCustomerByAccountNumber(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(transactionService.deposit(any())).thenReturn(DataUtils.getNotFoundResult());
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT+DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.depositData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
    }


    @Test
    void test_listTransaction_success() throws Exception {
        when(localStorage.listTransaction()).thenReturn(DataUtils.getTransactions());
        when(localStorage.findAccount(any())).thenReturn(Optional.of(DataUtils.accounts()));
        when(transactionService.listTransaction(any())).thenReturn(DataUtils.getTransactionResult());
        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ROOT+LIST_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<TransactionResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),DataUtils.getTransactions().size());
    }


    @Test
    void listTransactionByDate() throws Exception {
        when(localStorage.listTransaction()).thenReturn(DataUtils.getTransactions());
        when(localStorage.findAccount(any())).thenReturn(Optional.of(DataUtils.accounts()));
        when(transactionService.listTransactionByDate(any(),any())).thenReturn(DataUtils.getTransactionResult());
        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ROOT+TRANSACTION_LIST_BY_ACCOUNT+"?"+ACCOUNT_PARAM+"=123456789&"+DATE_PARAM+"=2022-01-10")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<TransactionResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),DataUtils.getTransactions().size());
    }
}
