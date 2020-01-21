package org.idorashau;

import com.sun.net.httpserver.HttpServer;
import org.idorashau.revoluttest.config.Configuration;
import org.idorashau.revoluttest.dto.Account;
import org.idorashau.revoluttest.dto.TransferRequest;
import org.idorashau.revoluttest.handlers.AccountHandler;
import org.idorashau.revoluttest.handlers.TransferHandler;
import org.idorashau.revoluttest.repos.AccountRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

import static org.idorashau.revoluttest.config.Configuration.*;

public class Application {

    private static final int serverPort = 8088;

    private static void createAccounts(AccountRepository accountRepository) {
        accountRepository.addAccount(new Account(UUID.randomUUID(), 100, "USD"));
        accountRepository.addAccount(new Account(UUID.randomUUID(), 1000, "EUR"));
        accountRepository.addAccount(new Account(UUID.randomUUID(), 50, "BTC"));
        accountRepository.addAccount(new Account(UUID.randomUUID(), 3333, "BYN"));
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        createAccounts(getAccountRepository());

        AccountHandler accountHandler = new AccountHandler(getAccountRepository(), getObjectMapper());
        TransferHandler transferHandler = new TransferHandler(getAccountRepository(), getObjectMapper());
        server.createContext("/api/accounts", accountHandler::getAccounts);
        server.createContext("/api/transfer", transferHandler::transfer);

        server.setExecutor(null);
        server.start();
        System.out.println("Application is started on port " + serverPort);
    }
}
