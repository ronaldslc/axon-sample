package com.example.demo.rest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.example.demo.api.CreateOrderCommand;
import com.example.demo.api.FetchOrdersQuery;
import com.example.demo.api.Order;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping
    public CompletableFuture<String> add() {
        String orderId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(orderId, productId));

    }

    @GetMapping
    public CompletableFuture<List<Order>> list() {
        return queryGateway.query(new FetchOrdersQuery(0, 20), ResponseTypes.multipleInstancesOf(Order.class));
    }
}
