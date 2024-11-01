package com.recargas.veci.controllers;
import com.recargas.veci.models.RechargeRequest;
import com.recargas.veci.models.Supplier;
import com.recargas.veci.models.TransactionResponse;
import com.recargas.veci.services.RechargeService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.recargas.veci.services.TransactionService;

import java.util.List;

@RestController
@CrossOrigin(origins = "vecirecargas-svzuxwt2u-pixelweb-co1s-projects.vercel.app")
@RequestMapping("/api/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/auth")
    public ResponseEntity<String> authenticate() {
        String token = rechargeService.authenticate();
        return ResponseEntity.ok(token);
    }

    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getSuppliers(@RequestHeader("Authorization") String token) {
        List<Supplier> suppliers = rechargeService.getSuppliers(token);
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping("/buy")
    public ResponseEntity<TransactionResponse> buyRecharge(
            @RequestHeader("Authorization") String token,
            @RequestBody RechargeRequest rechargeRequest) {

        // Validaciones
        if (!rechargeRequest.getCellPhone().matches("^3\\d{9}$"))
            return ResponseEntity.badRequest().body(new TransactionResponse("Número de teléfono inválido", null, null, 0));
        if (rechargeRequest.getValue() < 1000 || rechargeRequest.getValue() > 100000) {
            return ResponseEntity.badRequest().body(new TransactionResponse("Valor fuera de rango", null, null, 0));
        }

        // Llamada al servicio para procesar la recarga
        TransactionResponse transactionResponse = rechargeService.buyRecharge(token, rechargeRequest);

        return ResponseEntity.ok(transactionResponse);
    }


}

