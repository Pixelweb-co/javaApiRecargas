package com.recargas.veci.services;

import com.recargas.veci.models.RechargeRequest;
import com.recargas.veci.models.Supplier;
import com.recargas.veci.models.Transaction;
import com.recargas.veci.models.TransactionResponse;
import com.recargas.veci.repository.TransactionRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RechargeService {

    private final String apiUrl = "https://us-central1-puntored-dev.cloudfunctions.net/technicalTest-developer/api";
    private final RestTemplate restTemplate;

    private final TransactionRepository transactionRepository;

    public RechargeService(RestTemplateBuilder restTemplateBuilder, TransactionRepository transactionRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.transactionRepository = transactionRepository;
    }

    public String authenticate() {
        String url = apiUrl + "/auth";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "mtrQF6Q11eosqyQnkMY0JGFbGqcxVg5icvfVnX1ifIyWDvwGApJ8WUM8nHVrdSkN");
        Map<String, String> requestBody = Map.of("user", "user0147", "password", "#3Q34Sh0NlDS");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});
        return Objects.requireNonNull(response.getBody()).get("token");
    }

    public List<Supplier> getSuppliers(String token) {
        String url = apiUrl + "/getSuppliers";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Supplier>> response = restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Supplier>>() {});
        return response.getBody();
    }

    public TransactionResponse buyRecharge(String token, RechargeRequest rechargeRequest) {
        String url = apiUrl + "/buy";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<RechargeRequest> request = new HttpEntity<>(rechargeRequest, headers);
        ResponseEntity<TransactionResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, TransactionResponse.class);

        TransactionResponse transactionResponse = response.getBody();

        // Guardar la transacción en la base de datos
        if (transactionResponse != null) {
            Transaction transaction = new Transaction();
            transaction.setTransactionalID(transactionResponse.getTransactionalID());
            transaction.setCellPhone(rechargeRequest.getCellPhone());
            transaction.setValue(rechargeRequest.getValue());
            transaction.setMessage(transactionResponse.getMessage());
            transaction.setCreatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);
        }

        return transactionResponse;
    }
}
