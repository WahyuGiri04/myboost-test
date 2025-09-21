package com.myboost.test.service.impl;

import com.myboost.test.dto.request.PurchaseOrderDetailRequest;
import com.myboost.test.dto.request.PurchaseOrderRequest;
import com.myboost.test.dto.response.PurchaseOrderResponse;
import com.myboost.test.entity.Item;
import com.myboost.test.entity.PurchaseOrderDetail;
import com.myboost.test.entity.PurchaseOrderHeader;
import com.myboost.test.exception.ResourceNotFoundException;
import com.myboost.test.mapper.ItemMapper;
import com.myboost.test.mapper.PurchaseOrderMapper;
import com.myboost.test.repository.ItemRepository;
import com.myboost.test.repository.PurchaseOrderHeaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {

    @Mock
    private PurchaseOrderHeaderRepository headerRepository;

    @Mock
    private ItemRepository itemRepository;

    private PurchaseOrderServiceImpl purchaseOrderService;

    @BeforeEach
    void setUp() {
        purchaseOrderService = new PurchaseOrderServiceImpl(
                headerRepository,
                itemRepository,
                new PurchaseOrderMapper(new ItemMapper()));
    }

    @Test
    void findAll_returnsMappedResponses() {
        PurchaseOrderHeader header = sampleHeader();
        when(headerRepository.findAll()).thenReturn(List.of(header));

        List<PurchaseOrderResponse> responses = purchaseOrderService.findAll();

        assertThat(responses).hasSize(1);
        PurchaseOrderResponse response = responses.getFirst();
        assertThat(response.id()).isEqualTo(header.getId());
        assertThat(response.details()).hasSize(1);
        verify(headerRepository).findAll();
    }

    @Test
    void findById_throws_whenMissing() {
        when(headerRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseOrderService.findById(100));
    }

    @Test
    void create_persistsHeaderWithCalculatedTotals() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        PurchaseOrderDetailRequest detailRequest = new PurchaseOrderDetailRequest(1, 2, 70, 100, "detailCreator", "detailUpdater");
        PurchaseOrderRequest request = new PurchaseOrderRequest(now, "Office supplies", List.of(detailRequest), "creator", "updater");
        Item item = buildItem(1, "Pen");
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(headerRepository.save(any(PurchaseOrderHeader.class))).thenAnswer(invocation -> {
            PurchaseOrderHeader saved = invocation.getArgument(0, PurchaseOrderHeader.class);
            saved.setId(50);
            return saved;
        });

        PurchaseOrderResponse response = purchaseOrderService.create(request);

        ArgumentCaptor<PurchaseOrderHeader> captor = ArgumentCaptor.forClass(PurchaseOrderHeader.class);
        verify(headerRepository).save(captor.capture());
        verify(itemRepository).findById(1);
        PurchaseOrderHeader savedHeader = captor.getValue();
        assertThat(savedHeader.getTotalCost()).isEqualTo(140);
        assertThat(savedHeader.getTotalPrice()).isEqualTo(200);
        assertThat(savedHeader.getDetails()).hasSize(1);
        assertThat(response.totalCost()).isEqualTo(140);
        assertThat(response.totalPrice()).isEqualTo(200);
    }

    @Test
    void create_throws_whenItemMissing() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        PurchaseOrderDetailRequest detailRequest = new PurchaseOrderDetailRequest(5, 1, 10, 12, "creator", "creator");
        PurchaseOrderRequest request = new PurchaseOrderRequest(now, "Test", List.of(detailRequest), "creator", "creator");
        when(itemRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> purchaseOrderService.create(request));
    }

    @Test
    void update_replacesDetailsAndRecalculatesTotals() {
        PurchaseOrderHeader existing = sampleHeader();
        when(headerRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(headerRepository.save(existing)).thenAnswer(invocation -> invocation.getArgument(0));

        Item newItem = buildItem(2, "Notebook");
        when(itemRepository.findById(2)).thenReturn(Optional.of(newItem));

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        PurchaseOrderDetailRequest newDetail = new PurchaseOrderDetailRequest(2, 3, 40, 60, "creator", "updater");
        PurchaseOrderRequest request = new PurchaseOrderRequest(now, "Updated order", List.of(newDetail), "creator", "updater");

        PurchaseOrderResponse response = purchaseOrderService.update(existing.getId(), request);

        verify(headerRepository).save(existing);
        assertThat(existing.getDetails()).hasSize(1);
        assertThat(existing.getDetails().getFirst().getItem().getId()).isEqualTo(2);
        assertThat(existing.getTotalCost()).isEqualTo(120);
        assertThat(existing.getTotalPrice()).isEqualTo(180);
        assertThat(response.totalCost()).isEqualTo(120);
        assertThat(response.description()).isEqualTo("Updated order");
    }

    private PurchaseOrderHeader sampleHeader() {
        Item item = buildItem(1, "Pen");
        PurchaseOrderDetail detail = new PurchaseOrderDetail();
        detail.setItem(item);
        detail.setItemQty(1);
        detail.setItemCost(20);
        detail.setItemPrice(30);
        detail.setCreatedBy("detailCreator");

        PurchaseOrderHeader header = new PurchaseOrderHeader();
        header.setId(10);
        header.setPoDatetime(OffsetDateTime.now(ZoneOffset.UTC));
        header.setDescription("Sample order");
        header.setTotalCost(20);
        header.setTotalPrice(30);
        header.addDetail(detail);
        return header;
    }

    private Item buildItem(int id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription("desc");
        item.setCost(10);
        item.setPrice(15);
        return item;
    }
}
