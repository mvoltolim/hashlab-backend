package com.hash.hashlab.repositories;

import com.hash.hashlab.models.Product;
import com.hash.hashlab.utils.JacksonUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProductRepository {

    private final Table<Product> table;
    private final List<Integer> gifts;

    public ProductRepository() throws IOException {
        Product[] products = JacksonUtils.readFromFile("products.json", Product[].class);
        table = new Table<>(products);
        gifts = Stream.of(products).filter(Product::getIsGift).map(Product::getId).collect(Collectors.toList());
    }

    public Product getById(Integer id) {
        return table.get(id);
    }

    public Product getRandomGift() {
        return table.get(gifts.get(new Random().nextInt(gifts.size())));
    }

    public boolean anyGift(List<Integer> ids) {
        for (Integer id : ids) {
            if (gifts.contains(id))
                return true;
        }
        return false;
    }

}
