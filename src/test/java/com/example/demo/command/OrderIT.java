package com.example.demo.command;

import com.example.demo.api.CreateOrderCommand;
import com.example.demo.api.OrderConfirmedEvent;
import com.example.demo.api.OrderCreatedEvent;
import com.example.demo.api.OrderShippedEvent;
import com.example.demo.api.ShipOrderCommand;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * These order tests are integration tests that requires a working Axon Server
 */
class OrderIT {

    private static final String ORDER_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_ID = UUID.randomUUID().toString();

    private final FixtureConfiguration<OrderAggregate> testFixture = new AggregateTestFixture<>(OrderAggregate.class);

    @Test
    void testcreateOrderCommandPublishesOrderCreatedEvent() {
        testFixture.givenNoPriorActivity()
                   .when(new CreateOrderCommand(ORDER_ID, PRODUCT_ID))
                   .expectSuccessfulHandlerExecution()
                   .expectEvents(new OrderCreatedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void testShipOrderCommandThrowsUnconfirmedOrderExceptionForUnconfirmedOrder() {
        testFixture.given(new OrderCreatedEvent(ORDER_ID, PRODUCT_ID))
                   .when(new ShipOrderCommand(ORDER_ID))
                   .expectException(UnconfirmedOrderException.class);
    }

    @Test
    void testShipOrderCommandPublishesOrderShippedEvent() {
        testFixture.given(new OrderCreatedEvent(ORDER_ID, PRODUCT_ID), new OrderConfirmedEvent(ORDER_ID))
                   .when(new ShipOrderCommand(ORDER_ID))
                   .expectSuccessfulHandlerExecution()
                   .expectEvents(new OrderShippedEvent(ORDER_ID));
    }

}