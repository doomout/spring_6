package com.example.shopping.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("CatalogControllerIntegrationTest.sql")
class CatalogControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    // catalog/display-list 요청에 대한 테스트 메서드
    @Test
    void test_displayList() throws Exception {
        mockMvc.perform(
                get("/catalog/display-list")
        )
        .andExpect(content().string(containsString("지우개")))
        .andExpect(content().string(containsString("노트")))
        ;

    }

    // catalog/display-details 요청에 대한 테스트 메서드
    @Test
    void test_displayDetails() throws Exception {
        mockMvc.perform(
                        get("/catalog/display-details")
                                .param("productId", "p01")
                )
                .andExpect(content().string(containsString("지우개")))
        ;
    }
}