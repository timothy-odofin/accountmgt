package iobank.org.accountmgt.service;

import com.google.gson.internal.LinkedTreeMap;
import iobank.org.accountmgt.exception.BadRequestException;
import iobank.org.accountmgt.exception.DuplicationRecordException;
import iobank.org.accountmgt.exception.RecordNotFoundException;
import iobank.org.accountmgt.mapper.ModelMapper;
import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.request.AccountRequest;
import iobank.org.accountmgt.model.request.BlockAccountRequest;
import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.Accounts;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
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

import java.util.List;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.AccountEndpoints.*;
import static iobank.org.accountmgt.utils.MessageUtil.ACCOUNT_SUSPENDED_RESP;
import static iobank.org.accountmgt.utils.MessageUtil.DUPLICATE_ACCOUNT;
import static iobank.org.accountmgt.utils.MessageUtil.RECORD_NOT_FOUND;
import static iobank.org.accountmgt.utils.TestApiCode.*;
import static iobank.org.accountmgt.utils.TestMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class AccountServiceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LocalStorage localStorage;

    @AfterEach
    void init(){
        localStorage.init();//unset every data
    }
    @Test
    void test_add_customer_return_success() throws Exception {

        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), CREATED);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData(), CUSTOMER_CREATED);
        ApiResponse<List<CustomerResponse>> fetchList = accountService.listCustomer();
        assertEquals(fetchList.getData().size(), 1);


    }

    @Test
    void test_add_customer_return_bad_request() throws Exception {
        CustomerRequest payload = DataUtils.customerInvalidData();
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), AppValidator.isValid(payload));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            accountService.addCustomer(payload);
        }, AppValidator.isValid(payload));
        assertEquals(thrown.getMessage(), AppValidator.isValid(payload));


    }

    private void initCustomerData() {
        localStorage.save(ModelMapper.mapToCustomer(DataUtils.customerData()));
    }

    private void initAll(){
        localStorage.save(ModelMapper.mapToCustomer(DataUtils.customerData()));
        AccountRequest payload = DataUtils.accountRequest();
        localStorage.saveAccount(ModelMapper.mapToAccount(payload), payload.getCustomerPhone());

    }
    @Test
    void test_add_customer_return_duplicate_record() throws Exception {
        CustomerRequest payload = DataUtils.customerData();

        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), CREATED);
        assertEquals(result.getMessage(), SUCCESS);
        DuplicationRecordException thrown = assertThrows(DuplicationRecordException.class, () -> {
            accountService.addCustomer(payload);
        }, DUPLICATE_RECORD);
        assertEquals(thrown.getMessage(), DUPLICATE_RECORD);


    }


    @Test
    void test_add_account_to_customer_return_success() throws Exception {
        initCustomerData();
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.accountRequest())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<LinkedTreeMap<String, Object>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), CREATED);
        assertEquals(result.getMessage(), SUCCESS);
        log.info("{}", result.getData());
        assertEquals(result.getData().get("accountNumber"), "0580000016");
        ApiResponse<List<Accounts>> fetchList = accountService.listAllAccounts();
        assertEquals(fetchList.getData().size(), 1);


    }

    @Test
    void test_add_account_to_customer_return_bad_request() throws Exception {
        AccountRequest payload = DataUtils.accountInvalidRequest();
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), AppValidator.isValid(payload));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            accountService.addAccountToCustomer(payload);
        }, AppValidator.isValid(payload));
        assertEquals(thrown.getMessage(), AppValidator.isValid(payload));

    }

    @Test
    void test_add_account_to_customer_return_not_found() throws Exception {
        AccountRequest payload = DataUtils.accountInvalidRequest();
        payload.setCurrency("NGN");
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), RECORD_NOT_FOUND);
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            accountService.addAccountToCustomer(payload);
        }, RECORD_NOT_FOUND);
        assertEquals(thrown.getMessage(), RECORD_NOT_FOUND);

    }

    @Test
    void test_add_account_to_customer_return_duplicate_record() throws Exception {


        AccountRequest payload = DataUtils.accountRequest();
        mvc.perform(post(CUSTOMER_ROOT + ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.accountRequest())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), CREATED);
        assertEquals(result.getMessage(), SUCCESS);
        DuplicationRecordException thrown = assertThrows(DuplicationRecordException.class, () -> {
            accountService.addAccountToCustomer(payload);
        }, DUPLICATE_ACCOUNT);
        assertEquals(thrown.getMessage(), DUPLICATE_ACCOUNT);


    }

    @Test
    void test_suspendOrUnsuspendAccount_return_success() throws Exception {
        initCustomerData();
        AccountRequest payload = DataUtils.accountRequest();
        Accounts savedAccount = localStorage.saveAccount(ModelMapper.mapToAccount(payload), payload.getCustomerPhone());
        BlockAccountRequest load = DataUtils.blockAccount();
        load.setAccountNumber(savedAccount.getAccountNumber());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + SUSPEND_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(load)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData(), ACCOUNT_SUSPENDED_RESP);
        ApiResponse<List<Accounts>> fetchList = accountService.listAllAccounts();
        assertEquals(fetchList.getData().size(), 1);


    }

    @Test
    void test_suspendOrUnsuspendAccount_return_bad_request() throws Exception {
        BlockAccountRequest payload = DataUtils.blockInvalidAccount();
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + SUSPEND_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), AppValidator.isValid(payload));
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            accountService.retrieveAccount(payload.getAccountNumber());
        }, ACCOUNT_NOT_FOUND);
        assertEquals(thrown.getMessage(), ACCOUNT_NOT_FOUND);


    }

    @Test
    void test_suspendOrUnsuspendAccount_return_not_found() throws Exception {
        BlockAccountRequest payload = DataUtils.blockAccount();
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT + SUSPEND_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload)))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(), NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            accountService.suspendOrUnsuspendAccount(payload);
        }, RECORD_NOT_FOUND);
        assertEquals(thrown.getMessage(), RECORD_NOT_FOUND);


    }

    @Test
    void test_listCustomer_with_data() throws Exception {
        initCustomerData();
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+LIST_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<CustomerResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),DataUtils.getCustomerList().size());
        ApiResponse<List<CustomerResponse>> ls = accountService.listCustomer();
        assertFalse(ls.getData().isEmpty());
        assertEquals(ls.getData().size(),1);

    }

    @Test
    void test_listCustomer_with_no_data() throws Exception {
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+LIST_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<CustomerResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),0);
        ApiResponse<List<CustomerResponse>> ls = accountService.listCustomer();
        assertTrue(ls.getData().isEmpty());
        assertEquals(ls.getData().size(),0);

    }

    @Test
    void test_listAccountsByCustomer_success() throws Exception {
        initAll();
               MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+LIST_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<Accounts>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),DataUtils.getAccountList().size());
        ApiResponse<List<Accounts>> ls = accountService.listAccountsByCustomer("");
        assertFalse(ls.getData().isEmpty());
        assertEquals(ls.getData().size(),1);

    }
    @Test
    void test_listAccountsByCustomer_success_empty() throws Exception {

        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+LIST_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<Accounts>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),0);
        ApiResponse<List<Accounts>> ls = accountService.listAccountsByCustomer("");
        assertTrue(ls.getData().isEmpty());
        assertEquals(ls.getData().size(),0);

    }
    @Test
    void test_retrieveAccount_success() throws Exception {
        initAll();
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH_GET+"?"+ACCOUNT_PARAM+"=0580000016")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<LinkedTreeMap<String,Object>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertNotNull(result.getData());
        LinkedTreeMap<String,Object>  map = result.getData();
        assertFalse(map.isEmpty());
        assertEquals(map.get("accountNumber"),"0580000016");
        ApiResponse<AccountsResponse> results =accountService.retrieveAccount("0580000016");
        assertEquals(results.getData().getAccountNumber(),"0580000016");


    }
    @Test
    void test_retrieveAccount_fail() throws Exception {
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH_GET+"?"+ACCOUNT_PARAM+"=0580000016")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            accountService.retrieveAccount("0580000016");
        }, ACCOUNT_NOT_FOUND);
        assertEquals(thrown.getMessage(), ACCOUNT_NOT_FOUND);


    }

    @Test
    void test_RetrieveAccount_by_account_number_and_phone_success() throws Exception {
      initAll();
      String accountNumber="0580000016";
      String phone="07065990878";
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH+"?"+ACCOUNT_PARAM+"=0580000016&customerPhone=07065990878")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<AccountsResponse> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertNotNull(result.getData());
        ApiResponse<AccountsResponse> results= accountService.retrieveAccount(accountNumber,phone);
        assertEquals(results.getData().getAccountNumber(),accountNumber);

    }


    @Test
    void test_RetrieveAccount_by_account_number_and_phone_fail() throws Exception {
        initAll();
        String accountNumber="05800000";
        String phone="0706599087";
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH+"?"+ACCOUNT_PARAM+"=058000006&customerPhone=0706599087")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), ACCOUNT_NOT_FOUND);
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            accountService.retrieveAccount(accountNumber, phone);
        }, ACCOUNT_NOT_FOUND);
        assertEquals(thrown.getMessage(), ACCOUNT_NOT_FOUND);

    }
}
