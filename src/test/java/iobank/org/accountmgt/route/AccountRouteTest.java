package iobank.org.accountmgt.route;

import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.service.AccountService;
import iobank.org.accountmgt.service.TransactionService;
import iobank.org.accountmgt.storage.LocalStorage;
import iobank.org.accountmgt.utils.MessageUtil;
import iobank.org.accountmgt.validation.AppValidator;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.AccountEndpoints.ADD_PATH;
import static iobank.org.accountmgt.utils.AccountEndpoints.CUSTOMER_ROOT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AccountRoute.class)
class AccountRouteTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private LocalStorage localStorage;


    @Test
    void test_addCustomer() throws Exception {
        try (MockedStatic<AppValidator> utilities = Mockito.mockStatic(AppValidator.class)) {
            utilities.when(()->AppValidator.isValid(DataUtils.customerData())).thenReturn("");
        }
        when(localStorage.findCustomer(any())).thenReturn(Optional.empty());
        when(localStorage.save(any())).thenReturn(DataUtils.getStoreCustomer());
        when(accountService.addCustomer(any())).thenReturn(DataUtils.getResult());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CUSTOMER_ROOT+ADD_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<CustomerResponse> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),"200");
        assertEquals(result.getMessage(), MessageUtil.SUCCESS);
    }

    @Test
    void addAccountToCustomer() {
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
