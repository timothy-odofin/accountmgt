package iobank.org.accountmgt.integration;

import iobank.org.accountmgt.exception.AccountSuspendedException;
import iobank.org.accountmgt.exception.BadRequestException;
import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.request.DepositRequest;
import iobank.org.accountmgt.model.request.WithdrawalRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.Customer;
import iobank.org.accountmgt.model.response.TransactionResponse;
import iobank.org.accountmgt.service.TransactionService;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.validation.AppValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.MessageUtil.RECORD_NOT_FOUND;
import static iobank.org.accountmgt.utils.TestApiCode.*;
import static iobank.org.accountmgt.utils.TestMessages.*;
import static iobank.org.accountmgt.utils.TransactionEndpoints.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class TransactionServiceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LocalStorage localStorage;

    @AfterEach
    void init() {
        localStorage.init();//unset every data
    }

    private Customer initCustomerData() {
        return localStorage.save(ModelMapper.mapToCustomer(DataUtils.customerData()));
    }

    private Optional<Customer> getCustomer(Accounts account) {
        Customer ct = initCustomerData();
        localStorage.saveAccount(account,
                ct.getPhone());
        return localStorage.findCustomer(ct.getPhone());

    }

    @Test
    void test_withdraw_success() throws Exception {
        Optional<Customer> customerOptional = getCustomer(ModelMapper.mapToAccount(DataUtils.accountRequest()));
        assertTrue(customerOptional.isPresent());
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(DataUtils.withdrawalData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        Map<String,Object> map = result.getMeta();
        assertEquals(Double.valueOf(map.get("balance").toString()),5000);
    }

    @Test
    void test_withdraw_insufficient() throws Exception {
        Optional<Customer> customerOptional = getCustomer(ModelMapper.mapToAccount(DataUtils.accountRequest()));
        assertTrue(customerOptional.isPresent());
        WithdrawalRequest payload =DataUtils.withdrawalData();
        payload.setAmount(50000.0);
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),INSSUFICIENT );
        assertEquals(result.getMessage(), FAILED);
        LinkedHashMap<String,Accounts> map =customerOptional.get().getAccountMap();
        assertNotNull(map);
        assertNotNull(map.get(payload.getAccountNumber()));
        Accounts ac = map.get(payload.getAccountNumber());
        assertTrue(ac.getBalance()< payload.getAmount());

    }

    @Test
    void test_withdraw_account_suspended() throws Exception {
        Accounts accounts=ModelMapper.mapToAccount(DataUtils.accountRequest());
        accounts.setIsActive(false);
        Optional<Customer> customerOptional = getCustomer(accounts);
        assertTrue(customerOptional.isPresent());
        WithdrawalRequest payload =DataUtils.withdrawalData();
        payload.setAmount(50000.0);
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),ACCOUNT_SUS_CODE );
        assertEquals(result.getMessage(), FAILED);
        AccountSuspendedException thrown = assertThrows(AccountSuspendedException.class, () -> {
            transactionService.withdraw(payload);
        }, ACCOUNT_SUSPENDED);
        assertEquals(thrown.getMessage(), ACCOUNT_SUSPENDED);

    }

    @Test
    void test_withdraw_bad_request() throws Exception {
        WithdrawalRequest payload =DataUtils.withdrawalData();
        payload.setAmount(-50000.0);

        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST );
        assertEquals(result.getMessage(), FAILED);
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            transactionService.withdraw(payload);
        }, AppValidator.isValid(payload));
        assertEquals(thrown.getMessage(),  AppValidator.isValid(payload));

    }

    @Test
    void test_withdraw_customer_or_account_not_found() throws Exception {

        WithdrawalRequest payload =DataUtils.withdrawalData();
        payload.setAmount(50000.0);
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + WITHDRAWAL_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND );
        assertEquals(result.getMessage(), FAILED);
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            transactionService.withdraw(payload);
        }, RECORD_NOT_FOUND);
        assertEquals(thrown.getMessage(), RECORD_NOT_FOUND);

    }

    @Test
    void test_deposit_customer_account_not_found() throws Exception {
        DepositRequest payload =DataUtils.depositData();
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND );
        assertEquals(result.getMessage(), FAILED);
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            transactionService.deposit(payload);
        }, RECORD_NOT_FOUND);
        assertEquals(thrown.getMessage(), RECORD_NOT_FOUND);

    }

    @Test
    void test_deposit_bad_request() throws Exception {
        DepositRequest payload =DataUtils.depositInvalidData();
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST );
        assertEquals(result.getMessage(), FAILED);
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            transactionService.deposit(payload);
        }, AppValidator.isValid(payload));
        assertEquals(thrown.getMessage(),  AppValidator.isValid(payload));

    }
    @Test
    void test_deposit_success() throws Exception {
        Accounts oldAccount=ModelMapper.mapToAccount(DataUtils.accountRequest());
        Optional<Customer> customerOptional = getCustomer(oldAccount);
        assertTrue(customerOptional.isPresent());
        DepositRequest payload =DataUtils.depositData();
        MvcResult mvcResult = mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        LinkedHashMap<String,Accounts> map =customerOptional.get().getAccountMap();
        assertNotNull(map);
        assertNotNull(map.get(payload.getAccountNumber()));
        Accounts ac = map.get(payload.getAccountNumber());
        assertNotEquals(ac.getBalance(), (oldAccount.getBalance() + payload.getAmount()));
    }

    @Test
    void test_listTransaction() throws Exception {
        Accounts oldAccount=ModelMapper.mapToAccount(DataUtils.accountRequest());
        Optional<Customer> customerOptional = getCustomer(oldAccount);
        assertTrue(customerOptional.isPresent());
        DepositRequest payload =DataUtils.depositData();
        mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();

        mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ROOT + LIST_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<TransactionResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),2);

    }

    @Test
    void test_listTransaction_by_date() throws Exception {
        Accounts oldAccount=ModelMapper.mapToAccount(DataUtils.accountRequest());
        Optional<Customer> customerOptional = getCustomer(oldAccount);
        assertTrue(customerOptional.isPresent());
        DepositRequest payload =DataUtils.depositData();
        mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();

        mvc.perform(post(TRANSACTION_ROOT + DEPOSIT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = mvc.perform(get(TRANSACTION_ROOT+ TRANSACTION_LIST_BY_ACCOUNT+"?"+ACCOUNT_PARAM+"=0580000016&"+DATE_PARAM+"="+ LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<TransactionResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),2);

    }


}
