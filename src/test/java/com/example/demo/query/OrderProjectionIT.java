package com.example.demo.query;

import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import com.example.demo.api.FetchOrdersQuery;
import com.example.demo.api.Order;
import com.example.demo.api.OrderCreatedEvent;
import com.example.demo.api.Order.OrderStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderProjectionIT {
    private OrderProjection testSubject;
    private final QueryUpdateEmitter updateEmitter = mock(QueryUpdateEmitter.class);

    @BeforeEach
    void setUp() {
        testSubject = new OrderProjection(updateEmitter);
    }

    @Test
    void testFetchOrderQueryReturnsAllCardSummaries() {
        var orderId = "001";
        var productId = UUID.randomUUID().toString();
        testSubject.on(new OrderCreatedEvent(orderId, productId));

        var orderId2 = "002";
        var productId2 = UUID.randomUUID().toString();
        testSubject.on(new OrderCreatedEvent(orderId2, productId2));

        List<Order> results = testSubject.handle(new FetchOrdersQuery(0, 100));
        assertEquals(2, results.size());

        Order firstResult = results.get(0);
        assertEquals(orderId, firstResult.getOrderId());
        assertEquals(productId, firstResult.getProductId());
        assertEquals(OrderStatus.CREATED, firstResult.getOrderStatus());

        Order secondResult = results.get(1);
        assertEquals(orderId2, secondResult.getOrderId());
        assertEquals(productId2, secondResult.getProductId());
        assertEquals(OrderStatus.CREATED, secondResult.getOrderStatus());
    }

    @Test
    void testFetchOrderQueryReturnsFirstEntryOnLimitedSetOfCardSummaries() {
        var orderId = "001";
        var productId = UUID.randomUUID().toString();
        testSubject.on(new OrderCreatedEvent(orderId, productId));
        // second entry
        testSubject.on(new OrderCreatedEvent("002", UUID.randomUUID().toString()));

        List<Order> results = testSubject.handle(new FetchOrdersQuery(0, 1));
        assertEquals(1, results.size());
        Order result = results.get(0);
        assertEquals(orderId, result.getOrderId());
        assertEquals(productId, result.getProductId());
        assertEquals(OrderStatus.CREATED, result.getOrderStatus());
    }

    @Test
    void testFetchOrderQueryReturnsSecondEntryOnLimitedSetOfCardSummaries() {
        var orderId = "002";
        var productId = UUID.randomUUID().toString();
        // first entry
        testSubject.on(new OrderCreatedEvent("001", UUID.randomUUID().toString()));
        // second entry
        testSubject.on(new OrderCreatedEvent(orderId, productId));

        List<Order> results = testSubject.handle(new FetchOrdersQuery(1, 1));
        assertEquals(1, results.size());
        Order result = results.get(0);
        assertEquals(orderId, result.getOrderId());
        assertEquals(productId, result.getProductId());
        assertEquals(OrderStatus.CREATED, result.getOrderStatus());
    }
}