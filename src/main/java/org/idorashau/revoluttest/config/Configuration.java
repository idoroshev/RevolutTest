package org.idorashau.revoluttest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.idorashau.revoluttest.repos.AccountRepository;

public class Configuration {

    private static final AccountRepository ACCOUNT_REPOSITORY = new AccountRepository();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static AccountRepository getAccountRepository() {
        return ACCOUNT_REPOSITORY;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
