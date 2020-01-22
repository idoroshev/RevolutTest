package org.idorashau.revoluttest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public abstract class Handler {

    private final ObjectMapper objectMapper;

    public Handler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected <T> T readRequest(InputStream is, Class<T> type) throws IOException {
        return objectMapper.readValue(is, type);
    }

    protected <T> byte[] writeResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(response);
    }
}
