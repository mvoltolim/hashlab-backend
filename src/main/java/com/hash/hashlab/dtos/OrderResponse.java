package com.hash.hashlab.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private final long totalAmount;
    private final long totalAmountWithDiscount;
    private final long totalDiscount;
    private final List<ProductResponse> products;

    public OrderResponse(List<ProductResponse> products) {
        this.products = products;
        this.totalAmount = products.stream().mapToLong(ProductResponse::getTotalAmount).sum();
        this.totalDiscount = products.stream().mapToLong(ProductResponse::getDiscount).sum();
        this.totalAmountWithDiscount = totalAmount - totalDiscount;
    }

}
