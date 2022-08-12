package iobank.org.accountmgt.route;

import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.service.AccountService;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.validation.AppValidator;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.AccountEndpoints.*;
import static iobank.org.accountmgt.utils.TestApiCode.*;
import static iobank.org.accountmgt.utils.TestMessages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountRoute.class)
class AccountRouteTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private LocalStorage localStorage;


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
    void suspendOrUnsuspendAccount() {
    }

    @Test
    void listCustomer() {
    }

    @Test
    void listAccountsByCustomer() {
    }

    @Test
    void retrieveAccount() {
    }

    @Test
    void testRetrieveAccount() {
    }
}
