package com.example.demo.api;

import lombok.Value;

@Value
public class OrderConfirmedEvent {
    String orderId;
}
