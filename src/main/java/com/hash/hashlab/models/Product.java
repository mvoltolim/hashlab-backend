package com.hash.hashlab.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Entity {

    private Integer id;
    private String title;
    private String description;
    private Long amount;
    private Boolean isGift;

}
