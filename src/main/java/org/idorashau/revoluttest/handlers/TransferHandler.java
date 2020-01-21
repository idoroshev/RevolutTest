package org.idorashau.revoluttest.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.idorashau.revoluttest.Handler;
import org.idorashau.revoluttest.dto.Account;
import org.idorashau.revoluttest.repos.AccountRepository;

public class TransferHandler extends Handler {

    private final AccountRepository accountRepository;

    public TransferHandler(AccountRepository accountRepository, ObjectMapper objectMapper) {
        super(objectMapper);
        this.accountRepository = accountRepository;
    }

    public void transfer(HttpExchange exchange) {
//        Account sourceAccount = accountRepository.getAccounts().get(request.getSourceAccountId());
//        Account destinationAccount = accountRepository.getAccounts().get(request.getDestinationAccountId());
//        if (sourceAccount.getBalance() >= request.getQuantity()) {
//            sourceAccount.setBalance(sourceAccount.getBalance() - request.getQuantity());
//            destinationAccount.setBalance(destinationAccount.getBalance() + request.getQuantity());
//        }
    }
}
