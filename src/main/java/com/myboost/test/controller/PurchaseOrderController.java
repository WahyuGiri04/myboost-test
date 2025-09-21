package com.myboost.test.controller;

import com.myboost.test.dto.request.PurchaseOrderRequest;
import com.myboost.test.dto.response.PurchaseOrderResponse;
import com.myboost.test.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponse>> getPurchaseOrders(
            @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            return ResponseEntity.ok(Collections.singletonList(purchaseOrderService.findById(id)));
        }
        return ResponseEntity.ok(purchaseOrderService.findAll());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrder(
            @PathVariable Integer id,
            @Valid @RequestBody PurchaseOrderRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Integer id) {
        purchaseOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
