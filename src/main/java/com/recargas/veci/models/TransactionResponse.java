package com.recargas.veci.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
    private String message;
    private String transactionalID;
    private String cellPhone;
    private int value;

    // Constructor
    public TransactionResponse(String message, String transactionalID, String cellPhone, int value) {
        this.message = message;
        this.transactionalID = transactionalID;
        this.cellPhone = cellPhone;
        this.value = value;
    }

    // Constructor vacío (opcional, por si necesitas crear instancias sin parámetros)
    public TransactionResponse() {}
}
