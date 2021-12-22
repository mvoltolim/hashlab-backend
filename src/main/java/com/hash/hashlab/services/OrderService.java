package com.hash.hashlab.services;

import com.hash.hashlab.dtos.OrderRequest;
import com.hash.hashlab.dtos.OrderResponse;
import com.hash.hashlab.dtos.ProductRequest;
import com.hash.hashlab.dtos.ProductResponse;
import com.hash.hashlab.exceptions.CartContainsGiftException;
import com.hash.hashlab.grpc.DiscountClient;
import com.hash.hashlab.models.Product;
import com.hash.hashlab.repositories.ProductRepository;
import com.hash.hashlab.utils.DateProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    final ProductRepository productRepository;
    final DiscountClient discountClient;
    final DateProvider dateProvider;

    public OrderService(ProductRepository productRepository, DiscountClient discountClient, DateProvider dateProvider) {
        this.productRepository = productRepository;
        this.discountClient = discountClient;
        this.dateProvider = dateProvider;
    }

    public OrderResponse buildOrder(OrderRequest order) {
        List<Integer> ids = order.getProducts().stream().map(ProductRequest::getId).collect(Collectors.toList());
        if (productRepository.anyGift(ids)) {
            throw new CartContainsGiftException();
        }

        List<ProductResponse> productsResponse = new ArrayList<>(order.getProducts().size());

        order.getProducts().forEach(p -> {
            Product product = productRepository.getById(p.getId());
            float discount = discountClient.getPercentDiscount(p.getId());
            productsResponse.add(new ProductResponse(product.getId(), p.getQuantity(), product.getAmount(), discount, product.getIsGift()));
        });

        if (dateProvider.isBlackFriday()) {
            productsResponse.add(getGiftBlackFriday());
        }

        return new OrderResponse(productsResponse);
    }

    private ProductResponse getGiftBlackFriday() {
        Product gift = productRepository.getRandomGift();
        return new ProductResponse(gift.getId(), 1, 0, 0, true);
    }

}
