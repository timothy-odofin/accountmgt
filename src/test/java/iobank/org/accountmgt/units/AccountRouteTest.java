package iobank.org.accountmgt.units;

import com.google.gson.internal.LinkedTreeMap;
import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.response.AccountsResponse;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.service.AccountService;
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
import static iobank.org.accountmgt.utils.AccountEndpoints.*;
import static iobank.org.accountmgt.utils.MessageUtil.RECORD_NOT_FOUND;
import static iobank.org.accountmgt.utils.TestApiCode.*;
import static iobank.org.accountmgt.utils.TestMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountRoute.class)
@Slf4j
class AccountRouteTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;


    @MockBean
    private AccountService accountService;
    @MockBean
    private LocalStorage localStorage;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void test_add_customer_return_success() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.customerData())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.empty());
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(accountService.addCustomer(any())).thenReturn(DataUtils.getResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<CustomerResponse> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
    }

    @Test
    void test_add_customer_return_bad_request() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.customerData())).thenReturn(DataUtils.badRequestData());
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.empty());
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(accountService.addCustomer(any())).thenReturn(DataUtils.getBadRequestResult());
        MvcResult mvcResult=   mvc.perform(post(CUSTOMER_ROOT+ADD_PATH)
                        .content(mapToJson(DataUtils.customerData()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(),BADE_REQUEST);
    }
    @Test
    void test_add_customer_return_duplicate_record() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.customerData())).thenReturn(DataUtils.badRequestData());
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.empty());
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(accountService.addCustomer(any())).thenReturn(DataUtils.getDuplicateResult());
        MvcResult mvcResult=   mvc.perform(post(CUSTOMER_ROOT+ADD_PATH)
                        .content(mapToJson(DataUtils.customerData()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),DUPLICATE);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(),DUPLICATE_RECORD);
    }
    @Test
    void test_add_account_to_customer_return_success() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.accountRequest())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.addAccountToCustomer(any())).thenReturn(DataUtils.getResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
    }

    @Test
    void test_add_account_to_customer_return_bad_request() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.accountRequest())).thenReturn(DataUtils.badRequestData());
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.addAccountToCustomer(any())).thenReturn(DataUtils.getBadRequestResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), BADE_REQUEST);
    }
    @Test
    void test_add_account_to_customer_return_duplicate_record() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.accountRequest())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.addAccountToCustomer(any())).thenReturn(DataUtils.getDuplicateResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),DUPLICATE);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), DUPLICATE_RECORD);
    }
    @Test
    void test_add_account_to_customer_return_not_found() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.accountRequest())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.addAccountToCustomer(any())).thenReturn(DataUtils.getNotFoundResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), RECORD_NOT_FOUND);
    }
    @Test
    void test_suspendOrUnsuspendAccount_return_success() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.blockAccount())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.isAccountExists(any())).thenReturn(false);
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.suspendOrUnsuspendAccount(any())).thenReturn(DataUtils.getAccountSuspendedResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+SUSPEND_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.blockAccount())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
    }

    @Test
    void test_suspendOrUnsuspendAccount_return_bad_request() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.blockAccount())).thenReturn(BADE_REQUEST);
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.isAccountExists(any())).thenReturn(false);
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.suspendOrUnsuspendAccount(any())).thenReturn(DataUtils.getBadRequestResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+SUSPEND_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.blockAccount())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),BAD_REQUEST);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), BADE_REQUEST);
    }

    @Test
    void test_suspendOrUnsuspendAccount_return_not_found() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.blockAccount())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.of(DataUtils.getStoreCustomer()));
        when(localStorage.isAccountExists(any())).thenReturn(false);
        when(localStorage.saveAccount(any(),any())).thenReturn(DataUtils.accounts());
        when(accountService.suspendOrUnsuspendAccount(any())).thenReturn(DataUtils.getNotFoundResult());
        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+SUSPEND_ACCOUNT_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.blockAccount())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(),RECORD_NOT_FOUND);
    }
    @Test
    void test_listCustomer_with_data() throws Exception {
        when(localStorage.findAll()).thenReturn(Collections.singletonList(DataUtils.getStoreCustomer()));
        when(accountService.listCustomer()).thenReturn(DataUtils.getListCustomerResult());
              MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+LIST_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<CustomerResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),DataUtils.getCustomerList().size());
    }

    @Test
    void test_listAccountsByCustomer_success() throws Exception {
        when(localStorage.listAccount(any())).thenReturn(Collections.singletonList(DataUtils.accounts()));
        when(accountService.listAccountsByCustomer(any())).thenReturn(DataUtils.getListAccountResult());
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+LIST_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<List<AccountsResponse>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData().size(),DataUtils.getAccountList().size());
    }

    @Test
    void test_retrieveAccount_success() throws Exception {
        when(localStorage.findAccount(any())).thenReturn(Optional.of(DataUtils.accounts()));
        when(localStorage.findTransactionByAccountNumber(any())).thenReturn(DataUtils.getTransactions());
        when(accountService.retrieveAccount(any())).thenReturn(DataUtils.retrieveAccountResult());
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH_GET+"?"+ACCOUNT_PARAM+"=1234567898")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<LinkedTreeMap<String,Object>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertNotNull(result.getData());
        LinkedTreeMap<String,Object>  map = result.getData();
        assertFalse(map.isEmpty());
        assertEquals(map.get("accountNumber"),"0580000016");

    }
    @Test
    void test_retrieveAccount_fail() throws Exception {
        when(localStorage.findAccount(any())).thenReturn(Optional.of(DataUtils.accounts()));
        when(localStorage.findTransactionByAccountNumber(any())).thenReturn(DataUtils.getTransactions());
        when(accountService.retrieveAccount(any())).thenReturn(DataUtils.retrieveFailedAccountResult());
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH_GET+"?"+ACCOUNT_PARAM+"=1234567898")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(), ACCOUNT_NOT_FOUND);


    }

    @Test
    void test_RetrieveAccount_by_account_number_and_phone_success() throws Exception {
        when(localStorage.findAccountByNumber(any(),any())).thenReturn(Optional.of(DataUtils.accounts()));
        when(localStorage.findTransactionByAccountNumber(any())).thenReturn(DataUtils.getTransactions());
        when(accountService.retrieveAccount(any(),any())).thenReturn(DataUtils.retrieveAccountResult());
        MvcResult mvcResult = mvc.perform(get(CUSTOMER_ROOT+ACCOUNT_PATH+"?"+ACCOUNT_PARAM+"=1234567898&customerPhone=07065990878")
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<LinkedTreeMap<String,Object>> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), SUCCESS);
        assertNotNull(result.getData());
        LinkedTreeMap<String,Object>  map = result.getData();
        assertFalse(map.isEmpty());
        assertEquals(map.get("accountNumber"),"0580000016");
    }

    @Test
    void test_RetrieveAccount_by_account_number_and_phone_fail() throws Exception {
        when(localStorage.findAccountByNumber(any(),any())).thenReturn(Optional.of(DataUtils.accounts()));
        when(localStorage.findTransactionByAccountNumber(any())).thenReturn(DataUtils.getTransactions());
        when(accountService.retrieveAccount(any(),any())).thenReturn(DataUtils.retrieveFailedAccountResult());
       String url=CUSTOMER_ROOT+ACCOUNT_PATH+"?"+ACCOUNT_PARAM+"=1234567898&customerPhone=07065990878";
        MvcResult mvcResult = mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),NOT_FOUND);
        assertEquals(result.getMessage(), FAILED);
        assertNotNull(result.getData());
        assertEquals(result.getData(),ACCOUNT_NOT_FOUND);


    }
}
