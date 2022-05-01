package com.example.demo.api;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Order model
 */
@Value
public class Order {
    public enum OrderStatus { CREATED, CONFIRMED, SHIPPED };

    String orderId;
    String productId;

    @NonFinal
    @Setter
    OrderStatus orderStatus;
}
