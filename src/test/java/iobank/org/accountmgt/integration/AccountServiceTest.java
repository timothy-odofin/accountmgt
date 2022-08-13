package iobank.org.accountmgt.integration;

import iobank.org.accountmgt.model.DataUtils;
import iobank.org.accountmgt.model.response.ApiResponse;
import iobank.org.accountmgt.model.response.CustomerResponse;
import iobank.org.accountmgt.service.AccountService;
import iobank.org.accountmgt.storage.LocalStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static iobank.org.accountmgt.model.RestMapper.mapFromJson;
import static iobank.org.accountmgt.model.RestMapper.mapToJson;
import static iobank.org.accountmgt.utils.AccountEndpoints.ADD_PATH;
import static iobank.org.accountmgt.utils.AccountEndpoints.CUSTOMER_ROOT;
import static iobank.org.accountmgt.utils.TestApiCode.CREATED;
import static iobank.org.accountmgt.utils.TestApiCode.OKAY;
import static iobank.org.accountmgt.utils.TestMessages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountServiceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LocalStorage localStorage;

    @Test
    void test_add_customer_return_success() throws Exception {

        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),CREATED);
        assertEquals(result.getMessage(), SUCCESS);
        assertEquals(result.getData(),CUSTOMER_CREATED);
        ApiResponse<List<CustomerResponse>> fetchList =accountService.listCustomer();
        assertEquals(fetchList.getData().size(),1);
        localStorage.init();//unset every data

    }

    @Test
    void test_add_customer_return_bad_request() throws Exception {

        MvcResult mvcResult = mvc.perform(post(CUSTOMER_ROOT+ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(DataUtils.customerData())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse<String> result = mapFromJson(content, ApiResponse.class);
        assertEquals(result.getCode(),OKAY);
        assertEquals(result.getMessage(), FAILED);
        assertEquals(result.getData(),CUSTOMER_CREATED);
        ApiResponse<List<CustomerResponse>> fetchList =accountService.listCustomer();
        assertEquals(fetchList.getData().size(),1);
        localStorage.init();//unset every data

    }


}
