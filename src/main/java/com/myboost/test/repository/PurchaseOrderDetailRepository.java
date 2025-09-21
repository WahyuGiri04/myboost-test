package com.myboost.test.repository;

import com.myboost.test.entity.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Integer> {
}
