package org.idorashau.revoluttest.utils;

public class ResponseInfo {

    private int code;
    private String rejectionReason;

    public ResponseInfo() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
