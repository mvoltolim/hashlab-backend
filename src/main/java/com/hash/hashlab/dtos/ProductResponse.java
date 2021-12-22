package com.hash.hashlab.dtos;

import lombok.Data;

@Data
public class ProductResponse {

    private final int id;
    private final int quantity;
    private final long unitAmount;
    private final long totalAmount;
    private final long discount;
    private final Boolean isGift;

    public ProductResponse(int id, int quantity, long unitAmount, float percentDiscount, boolean isGift) {
        this.id = id;
        this.quantity = quantity;
        this.unitAmount = unitAmount;
        this.totalAmount = quantity * unitAmount;
        this.discount = (long) (totalAmount * percentDiscount);
        this.isGift = isGift;
    }

}
