package com.recargas.veci.services;

import com.recargas.veci.models.Transaction;
import com.recargas.veci.models.TransactionResponse;
import com.recargas.veci.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;  // Cambiar a no estático

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getMessage(),
                        transaction.getTransactionalID(),
                        transaction.getCellPhone(),
                        transaction.getValue()
                )).collect(Collectors.toList());
    }
}
