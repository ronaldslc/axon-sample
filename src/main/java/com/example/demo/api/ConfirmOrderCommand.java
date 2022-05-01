package com.example.demo.api;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class ConfirmOrderCommand {
    @TargetAggregateIdentifier
    String orderId;

    String productId;
}
