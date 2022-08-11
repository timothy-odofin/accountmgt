package iobank.org.accountmgt.route;

import iobank.org.accountmgt.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(TransactionRoute.class)
class TransactionRouteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void withdraw() {
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
