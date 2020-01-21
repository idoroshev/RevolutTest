package org.idorashau.revoluttest.repos;

import org.idorashau.revoluttest.dto.Account;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {

    private static final Map<UUID, Account> accountMap = new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        accountMap.put(account.getAccountId(), account);
    }

    public Map<UUID, Account> getAccounts() {
        return accountMap;
    }
}
