package com.example.demo.api;

import lombok.Value;

@Value
public class OrderShippedEvent {
    String orderId;
}
