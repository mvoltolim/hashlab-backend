package com.hash.hashlab.controllers;

import com.hash.hashlab.dtos.OrderRequest;
import com.hash.hashlab.dtos.OrderResponse;
import com.hash.hashlab.exceptions.CartContainsGiftException;
import com.hash.hashlab.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("order")
    public ResponseEntity<OrderResponse> buildOrder(@RequestBody OrderRequest order) {
        OrderResponse orderResponse = service.buildOrder(order);
        return ResponseEntity.ok(orderResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CartContainsGiftException.class)
    public Map<String, String> handleBadRequest(HttpServletRequest req, Exception ex) {
        return Map.of("url", req.getRequestURL().toString(), "error", ex.getMessage());
    }

}
