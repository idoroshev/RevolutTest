package org.idorashau.revoluttest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Handler {

    private final ObjectMapper objectMapper;

    public Handler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected <T> byte[] writeResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(response);
    }
}
