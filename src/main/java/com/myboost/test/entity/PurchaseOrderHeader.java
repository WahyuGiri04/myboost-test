package com.myboost.test.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "po_h", schema = "procurement")
public class PurchaseOrderHeader extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "po_datetime", nullable = false)
    private OffsetDateTime poDatetime;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "total_cost", nullable = false)
    private Integer totalCost;

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseOrderDetail> details = new ArrayList<>();

    public void addDetail(PurchaseOrderDetail detail) {
        detail.setHeader(this);
        this.details.add(detail);
    }

    public void clearDetails() {
        for (PurchaseOrderDetail detail : details) {
            detail.setHeader(null);
        }
        this.details.clear();
    }
}
