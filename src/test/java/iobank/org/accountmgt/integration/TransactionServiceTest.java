package iobank.org.accountmgt.integration;

import iobank.org.accountmgt.service.TransactionService;
import iobank.org.accountmgt.storage.LocalStorage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
    void init(){
        localStorage.init();//unset every data
    }



}
