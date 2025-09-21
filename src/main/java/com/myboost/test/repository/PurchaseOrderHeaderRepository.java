package com.myboost.test.repository;

import com.myboost.test.entity.PurchaseOrderHeader;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderHeaderRepository extends JpaRepository<PurchaseOrderHeader, Integer> {

    @SuppressWarnings("null")
    @Override
    @EntityGraph(attributePaths = {"details", "details.item"})
    List<PurchaseOrderHeader> findAll();

    @SuppressWarnings("null")
    @Override
    @EntityGraph(attributePaths = {"details", "details.item"})
    Optional<PurchaseOrderHeader> findById(Integer integer);
}
