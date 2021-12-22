package com.hash.hashlab.services;

import com.hash.hashlab.dtos.OrderRequest;
import com.hash.hashlab.dtos.ProductRequest;
import com.hash.hashlab.exceptions.CartContainsGiftException;
import com.hash.hashlab.grpc.DiscountClient;
import com.hash.hashlab.models.Product;
import com.hash.hashlab.repositories.ProductRepository;
import com.hash.hashlab.utils.DateProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository repository;
    @Mock
    private DiscountClient discountClient;
    @Mock
    private DateProvider dateProvider;
    @InjectMocks
    private OrderService service;

    @Test
    void buildOrderWithGiftInCartThrowsException() {
        when(repository.anyGift(anyList())).thenReturn(true);

        var orderReq = new OrderRequest(List.of(new ProductRequest(1, 2)));

        var ex = Assertions.assertThrows(CartContainsGiftException.class, () -> {
            service.buildOrder(orderReq);
        });

        assertEquals("Cart contains gift!", ex.getMessage());

    }

    @Test
    void buildOrder() {
        when(repository.getById(1)).thenReturn(new Product(1, "P1", "P1", 100L, false));
        when(repository.getById(2)).thenReturn(new Product(2, "P2", "P2", 250L, false));
        when(discountClient.getPercentDiscount(1)).thenReturn(0.05F);
        when(discountClient.getPercentDiscount(2)).thenReturn(0F);

        var orderReq = new OrderRequest(List.of(new ProductRequest(1, 2), new ProductRequest(2, 1)));

        var result = service.buildOrder(orderReq);

        //then:
        assertEquals(2, result.getProducts().size());

        var firstProduct = result.getProducts().get(0);
        assertEquals(1, firstProduct.getId());
        assertEquals(2, firstProduct.getQuantity());
        assertEquals(100, firstProduct.getUnitAmount());
        assertEquals(200, firstProduct.getTotalAmount());
        assertEquals(10, firstProduct.getDiscount());
        assertFalse(firstProduct.getIsGift());

        var secondProduct = result.getProducts().get(1);
        assertEquals(2, secondProduct.getId());
        assertEquals(1, secondProduct.getQuantity());
        assertEquals(250, secondProduct.getUnitAmount());
        assertEquals(250, secondProduct.getTotalAmount());
        assertEquals(0, secondProduct.getDiscount());
        assertFalse(secondProduct.getIsGift());
    }

    @Test
    void buildOrderAtBlackFridayAddGiftInCart() {
        when(repository.getById(1)).thenReturn(new Product(1, "P1", "P1", 100L, false));
        when(discountClient.getPercentDiscount(1)).thenReturn(0.05F);
        when(repository.getRandomGift()).thenReturn(new Product(6, "GIFT", "GIFT", 100L, true));
        when(dateProvider.isBlackFriday()).thenReturn(true);

        var orderReq = new OrderRequest(List.of(new ProductRequest(1, 2)));

        var result = service.buildOrder(orderReq);

        //then:
        assertEquals(2, result.getProducts().size());

        var firstProduct = result.getProducts().get(0);
        assertEquals(1, firstProduct.getId());
        assertEquals(2, firstProduct.getQuantity());
        assertEquals(100, firstProduct.getUnitAmount());
        assertEquals(200, firstProduct.getTotalAmount());
        assertEquals(10, firstProduct.getDiscount());
        assertFalse(firstProduct.getIsGift());

        var secondProduct = result.getProducts().get(1);
        assertEquals(6, secondProduct.getId());
        assertEquals(1, secondProduct.getQuantity());
        assertEquals(0, secondProduct.getUnitAmount());
        assertEquals(0, secondProduct.getTotalAmount());
        assertEquals(0, secondProduct.getDiscount());
        assertTrue(secondProduct.getIsGift());
    }

}
