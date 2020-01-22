package org.idorashau.revoluttest.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.idorashau.revoluttest.Handler;
import org.idorashau.revoluttest.dto.Account;
import org.idorashau.revoluttest.dto.TransferEvent;
import org.idorashau.revoluttest.dto.TransferRequest;
import org.idorashau.revoluttest.repos.AccountRepository;
import org.idorashau.revoluttest.utils.ResponseInfo;

import java.io.IOException;
import java.io.OutputStream;

public class TransferHandler extends Handler {

    private final Object accountMonitor;
    private final AccountRepository accountRepository;

    public TransferHandler(AccountRepository accountRepository, ObjectMapper objectMapper, Object accountMonitor) {
        super(objectMapper);
        this.accountRepository = accountRepository;
        this.accountMonitor = accountMonitor;
    }

    private TransferEvent transfer(Account sourceAccount, Account destinationAccount,
                                   TransferRequest request, ResponseInfo responseInfo) {
        synchronized (accountMonitor) {
            // Assume that price is always 1.
            if (sourceAccount.getBalance() < request.getQuantity()) {
                responseInfo.setCode(400);
                responseInfo.setRejectionReason("Insufficient balance.");
                return null;
            } else {
                sourceAccount.setBalance(sourceAccount.getBalance() - request.getQuantity());
                destinationAccount.setBalance(destinationAccount.getBalance() + request.getQuantity());
                responseInfo.setCode(400);
                responseInfo.setRejectionReason(null);
                return new TransferEvent(sourceAccount.getAccountId(), destinationAccount.getAccountId(),
                        sourceAccount.getBalance(), destinationAccount.getBalance());
            }
        }
    }

    public void transfer(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            TransferRequest request = super.readRequest(exchange.getRequestBody(), TransferRequest.class);
            Account sourceAccount = accountRepository.getAccounts().get(request.getSourceId());
            Account destinationAccount = accountRepository.getAccounts().get(request.getDestinationId());

            if (sourceAccount == null) {
                exchange.sendResponseHeaders(400, 0);
                response = super.writeResponse("Cannot find account with AccountId = " + request.getSourceId());
            } else if (destinationAccount == null) {
                exchange.sendResponseHeaders(400, 0);
                response = super.writeResponse("Cannot find account with AccountId = " + request.getDestinationId());
            } else {
                ResponseInfo responseInfo = new ResponseInfo();
                TransferEvent transferEvent = transfer(sourceAccount, destinationAccount, request, responseInfo);

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(responseInfo.getCode(), 0);
                response = super.writeResponse(transferEvent != null ? transferEvent : responseInfo.getRejectionReason());
            }
        } else {
            exchange.sendResponseHeaders(405, 0);
            response = super.writeResponse(exchange.getRequestMethod() + " is not allowed for this address.");
        }
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
