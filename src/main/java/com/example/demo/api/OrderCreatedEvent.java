package com.example.demo.api;

import lombok.Value;

@Value
public class OrderCreatedEvent {
    String orderId;
    String productId;
}
