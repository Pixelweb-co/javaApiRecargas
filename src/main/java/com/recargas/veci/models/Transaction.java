package com.recargas.veci.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String transactionalID;
    @Getter
    @Setter
    private String cellPhone;
    @Getter
    @Setter
    private int value;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private LocalDateTime createdAt;

    public Transaction() {}

}
