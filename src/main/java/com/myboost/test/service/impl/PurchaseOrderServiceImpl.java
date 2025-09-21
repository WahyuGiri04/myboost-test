package com.myboost.test.service.impl;

import com.myboost.test.dto.request.PurchaseOrderDetailRequest;
import com.myboost.test.dto.request.PurchaseOrderRequest;
import com.myboost.test.dto.response.PurchaseOrderResponse;
import com.myboost.test.entity.Item;
import com.myboost.test.entity.PurchaseOrderDetail;
import com.myboost.test.entity.PurchaseOrderHeader;
import com.myboost.test.exception.ResourceNotFoundException;
import com.myboost.test.mapper.PurchaseOrderMapper;
import com.myboost.test.repository.ItemRepository;
import com.myboost.test.repository.PurchaseOrderHeaderRepository;
import com.myboost.test.service.PurchaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderHeaderRepository headerRepository;
    private final ItemRepository itemRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderHeaderRepository headerRepository,
                                    ItemRepository itemRepository,
                                    PurchaseOrderMapper purchaseOrderMapper) {
        this.headerRepository = headerRepository;
        this.itemRepository = itemRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> findAll() {
        return headerRepository.findAll().stream()
                .map(purchaseOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse findById(Integer id) {
        PurchaseOrderHeader header = getHeader(id);
        return purchaseOrderMapper.toResponse(header);
    }

    @Override
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {
        PurchaseOrderHeader header = new PurchaseOrderHeader();
        header.setCreatedBy(request.createdBy());
        applyRequestToHeader(request, header);
        PurchaseOrderHeader saved = headerRepository.save(header);
        return purchaseOrderMapper.toResponse(saved);
    }

    @Override
    public PurchaseOrderResponse update(Integer id, PurchaseOrderRequest request) {
        PurchaseOrderHeader header = getHeader(id);
        applyRequestToHeader(request, header);
        PurchaseOrderHeader saved = headerRepository.save(header);
        return purchaseOrderMapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!headerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Purchase order with id %d not found".formatted(id));
        }
        headerRepository.deleteById(id);
    }

    private void applyRequestToHeader(PurchaseOrderRequest request, PurchaseOrderHeader header) {
        header.setPoDatetime(request.poDatetime());
        header.setDescription(request.description());
        if (request.updatedBy() != null) {
            header.setUpdatedBy(request.updatedBy());
        }

        header.clearDetails();
        for (PurchaseOrderDetailRequest detailRequest : request.details()) {
            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            detail.setItem(resolveItem(detailRequest.itemId()));
            detail.setItemQty(detailRequest.itemQty());
            detail.setItemCost(detailRequest.itemCost());
            detail.setItemPrice(detailRequest.itemPrice());
            detail.setCreatedBy(detailRequest.createdBy());
            detail.setUpdatedBy(detailRequest.updatedBy());
            header.addDetail(detail);
        }

        header.setTotalCost(calculateTotalCost(header));
        header.setTotalPrice(calculateTotalPrice(header));
    }

    private int calculateTotalCost(PurchaseOrderHeader header) {
        return header.getDetails().stream()
                .mapToInt(detail -> detail.getItemCost() * detail.getItemQty())
                .sum();
    }

    private int calculateTotalPrice(PurchaseOrderHeader header) {
        return header.getDetails().stream()
                .mapToInt(detail -> detail.getItemPrice() * detail.getItemQty())
                .sum();
    }

    private Item resolveItem(Integer itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item with id %d not found".formatted(itemId)));
    }

    private PurchaseOrderHeader getHeader(Integer id) {
        return headerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order with id %d not found".formatted(id)));
    }
}
