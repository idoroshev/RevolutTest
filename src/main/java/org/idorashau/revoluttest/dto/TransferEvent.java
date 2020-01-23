package org.idorashau.revoluttest.dto;

import java.util.UUID;

public class TransferEvent {

    private UUID sourceId;
    private UUID destinationId;
    private long sourceBalance;
    private long destinationBalance;

    public TransferEvent() {
    }

    public TransferEvent(UUID sourceId, UUID destinationId, long sourceBalance, long destinationBalance) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.sourceBalance = sourceBalance;
        this.destinationBalance = destinationBalance;
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

    public long getSourceBalance() {
        return sourceBalance;
    }

    public void setSourceBalance(long sourceBalance) {
        this.sourceBalance = sourceBalance;
    }

    public long getDestinationBalance() {
        return destinationBalance;
    }

    public void setDestinationBalance(long destinationBalance) {
        this.destinationBalance = destinationBalance;
    }
}
