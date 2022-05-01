package com.example.demo.api;

import lombok.Value;

@Value
public class FetchOrdersQuery {
    int offset;
    int limit;
}
