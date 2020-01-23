package org.idorashau.revoluttest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.idorashau.revoluttest.dto.Account;
import org.idorashau.revoluttest.dto.TransferEvent;
import org.idorashau.revoluttest.dto.TransferRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TransferTest {

    private final String host = "localhost";
    private final int port = 8088;
    private final String baseUrl = "http://" + host + ":" + port;
    private final String accountsUrl = "/api/accounts";
    private final String transferUrl = "/api/transfer";

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {

    }

    private List<Account> getAccounts() throws IOException {
        HttpGet request = new HttpGet(baseUrl + accountsUrl);
        request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        List<Account> accounts;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            assertNotNull(entity);
            String result = EntityUtils.toString(entity);
            accounts = mapper.readValue(result, new TypeReference<List<Account>>() {});
            assertTrue(accounts != null && accounts.size() == 4);
        }
        return accounts;
    }

    private void sendSuccessTransfer(Account source, Account destination, long quantity) throws IOException {
        HttpPost post = new HttpPost(baseUrl + transferUrl);
        post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        TransferRequest transferRequest = new TransferRequest(source.getAccountId(), destination.getAccountId(), quantity);
        String json = mapper.writeValueAsString(transferRequest);
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            assertNotNull(entity);
            String result = EntityUtils.toString(entity);
            TransferEvent transferEvent = mapper.readValue(result, TransferEvent.class);
            assertEquals(transferRequest.getSourceId(), transferEvent.getSourceId());
            assertEquals(transferRequest.getDestinationId(), transferEvent.getDestinationId());
            assertEquals(source.getBalance() - transferRequest.getQuantity(), transferEvent.getSourceBalance());
            assertEquals(destination.getBalance() + transferRequest.getQuantity(), transferEvent.getDestinationBalance());
            source.setBalance(transferEvent.getSourceBalance());
            destination.setBalance(transferEvent.getDestinationBalance());
        }
    }

    private void sendFailedTransfer(Account source, Account destination, long quantity, int code, String reason) throws IOException {
        HttpPost post = new HttpPost(baseUrl + transferUrl);
        post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        TransferRequest transferRequest = new TransferRequest(source.getAccountId(), destination.getAccountId(), quantity);
        String json = mapper.writeValueAsString(transferRequest);
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            assertEquals(code, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            assertNotNull(entity);
            String result = EntityUtils.toString(entity);
            assertEquals(reason, result);
        }
    }

    @Test
    public void testTransferSuccess() throws IOException {
        List<Account> accounts = getAccounts();

        // Try to request a transfer.
        Account account1 = accounts.get(0);
        Account account2 = accounts.get(1);
        long oldBalance = account2.getBalance();
        sendSuccessTransfer(account1, account2, account1.getBalance() / 2);

        // Try to request a transfer that was unavailable before.
        Account account3 = accounts.get(2);
        sendSuccessTransfer(account2, account3, oldBalance + 1);
    }

    @Test
    public void testInsufficientQuantity() throws IOException {
        List<Account> accounts = getAccounts();

        // Try to request a transfer.
        Account account1 = accounts.get(0);
        Account account2 = accounts.get(1);
        sendFailedTransfer(account1, account2, account1.getBalance() + 1, 400, "\"Insufficient balance.\"");
    }

    @Test
    public void testEqualIds() throws IOException {
        List<Account> accounts = getAccounts();

        // Try to request a transfer.
        Account account1 = accounts.get(0);
        sendFailedTransfer(account1, account1, 1, 400, "\"SourceId and DestinationId are equal: " + account1.getAccountId() + "\"");
    }

    @Test
    public void testUnknownAccount() throws IOException {
        List<Account> accounts = getAccounts();

        // Try to request a transfer.
        Account account1 = accounts.get(0);
        Account newAccount = new Account(UUID.randomUUID(), 10, "XXX");
        sendFailedTransfer(account1, newAccount, 1, 400, "\"Cannot find account with AccountId = " + newAccount.getAccountId() + "\"");
    }
}
