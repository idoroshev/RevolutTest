package org.idorashau.revoluttest.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.idorashau.revoluttest.Handler;
import org.idorashau.revoluttest.dto.Account;
import org.idorashau.revoluttest.repos.AccountRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AccountHandler extends Handler {

    private final AccountRepository accountRepository;

    public AccountHandler(AccountRepository accountRepository, ObjectMapper objectMapper) {
        super(objectMapper);
        this.accountRepository = accountRepository;
    }

    public void getAccounts(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Account> accounts = new ArrayList<>(accountRepository.getAccounts().values());
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            response = super.writeResponse(accounts);
        } else {
            exchange.sendResponseHeaders(405, 0);
            response = super.writeResponse(exchange.getRequestMethod() + " is not allowed for this address.");
        }
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
