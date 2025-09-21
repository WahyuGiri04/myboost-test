package com.myboost.test.service;

import com.myboost.test.dto.request.PurchaseOrderRequest;
import com.myboost.test.dto.response.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {

    List<PurchaseOrderResponse> findAll();

    PurchaseOrderResponse findById(Integer id);

    PurchaseOrderResponse create(PurchaseOrderRequest request);

    PurchaseOrderResponse update(Integer id, PurchaseOrderRequest request);

    void delete(Integer id);
}
