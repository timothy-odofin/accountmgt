package iobank.org.accountmgt.service;

import iobank.org.accountmgt.storage.LocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements  AccountService{
    private final LocalStorage localStorage;
}