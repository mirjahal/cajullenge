package br.com.caju.domain;

public enum TransactionStatus {

    APPROVED("00"),
    INSUFFICIENT_BALANCE("51"),
    REJECTED("07");

    private final String code;

    TransactionStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
