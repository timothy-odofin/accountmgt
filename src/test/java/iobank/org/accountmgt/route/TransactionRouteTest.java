package iobank.org.accountmgt.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.service.AccountService;
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

import java.util.Optional;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.AccountEndpoints.ADD_PATH;
import static iobank.org.accountmgt.utils.AccountEndpoints.CUSTOMER_ROOT;
import static iobank.org.accountmgt.utils.TestApiCode.OKAY;
import static iobank.org.accountmgt.utils.TestMessages.SUCCESS;
import static iobank.org.accountmgt.utils.TransactionEndpoints.TRANSACTION_ROOT;
import static iobank.org.accountmgt.utils.TransactionEndpoints.WITHDRAWAL_PATH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    void deposit() {
    }

    @Test
    void listTransaction() {
    }

    @Test
    void listTransactionByDate() {
    }
}
