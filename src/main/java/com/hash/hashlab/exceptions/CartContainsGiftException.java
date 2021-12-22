package com.hash.hashlab.exceptions;

public class CartContainsGiftException extends RuntimeException {

    public CartContainsGiftException() {
        super("Cart contains gift!");
    }

}
