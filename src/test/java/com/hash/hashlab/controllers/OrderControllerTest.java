package com.hash.hashlab.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void buildOrder() throws Exception {
        //given:
        var requestBody = "{" +
                "  \"products\": [" +
                "    {" +
                "      \"id\": 1," +
                "      \"quantity\": 2" +
                "    }," +
                "    {" +
                "      \"id\": 2," +
                "      \"quantity\": 1" +
                "    }" +
                "  ]" +
                "}";

        //when:
        var result = mvc.perform(MockMvcRequestBuilders.post("/order").contentType(APPLICATION_JSON).content(requestBody));

        //then:
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_amount", is(124125)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_amount_with_discount", is(124125)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_discount", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].quantity", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].unit_amount", is(15157)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].total_amount", is(30314)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].discount", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].is_gift", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].quantity", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].unit_amount", is(93811)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].total_amount", is(93811)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].discount", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].is_gift", is(false)));
    }

}
