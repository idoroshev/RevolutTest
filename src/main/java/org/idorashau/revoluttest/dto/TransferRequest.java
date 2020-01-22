package org.idorashau.revoluttest.dto;

import java.util.UUID;

public class TransferRequest {

    private UUID sourceId;
    private UUID destinationId;
    private long quantity;

    public TransferRequest() {
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public UUID getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(UUID destinationId) {
        this.destinationId = destinationId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
