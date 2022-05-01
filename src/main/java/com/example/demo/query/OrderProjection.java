package com.example.demo.query;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import com.example.demo.api.FetchOrdersQuery;
import com.example.demo.api.Order;
import com.example.demo.api.OrderConfirmedEvent;
import com.example.demo.api.OrderCreatedEvent;
import com.example.demo.api.OrderShippedEvent;
import com.example.demo.api.Order.OrderStatus;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("query")
@Service
@ProcessingGroup("order")
public class OrderProjection {

    private final SortedMap<String, Order> orders;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public OrderProjection(QueryUpdateEmitter queryUpdateEmitter) {
        this.orders = new ConcurrentSkipListMap<>();
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        String orderId = event.getOrderId();
        orders.put(orderId, new Order(orderId, event.getProductId(), OrderStatus.CREATED));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        var order = orders.get(event.getOrderId());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        queryUpdateEmitter.emit(FetchOrdersQuery.class, query -> true, order);
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        var order = orders.get(event.getOrderId());
        order.setOrderStatus(OrderStatus.SHIPPED);
        queryUpdateEmitter.emit(FetchOrdersQuery.class, query -> true, order);
    }

    @QueryHandler
    public List<Order> handle(FetchOrdersQuery query) {
        Order[] ordersArray = orders.values().toArray(Order[]::new);
        return Arrays.stream(ordersArray, query.getOffset(), ordersArray.length)
                .limit(query.getLimit())
                .collect(Collectors.toList());
    }
}