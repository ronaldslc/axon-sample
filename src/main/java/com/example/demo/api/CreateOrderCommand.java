package com.example.demo.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    String orderId;
    String productId;
}
