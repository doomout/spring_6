package com.example.shopping.controller;

import com.example.shopping.entity.Product;
import com.example.shopping.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CatalogController.class) // CatalogController만 테스트 대상으로 지정
public class CatalogControllerTest {

    @Autowired
    MockMvc mockMvc; // MockMvc를 이용하여 컨트롤러 테스트

    @MockBean
    CatalogService catalogService; // CatalogService를 Mock으로 주입

    //displayList 테스트 코드
    @Test
    void test_displayList() throws Exception {
        // 가짜 상품 목록 생성
        List<Product> products = new ArrayList<Product>();

        Product product = new Product();
        product.setName("상품01");
        products.add(product);

        product = new Product();
        product.setName("상품02");
        products.add(product);

        // catalogService.findAll()이 호출되면 products 반환
        doReturn(products).when(catalogService).findAll();

        // 요청 & 응답 검증
        mockMvc.perform(
                get("/catalog/display-list")
                )
                .andExpect(content().string(containsString("상품01")))
                .andExpect(content().string(containsString("상품02")))
        ;
    }

    @Test
    void test_displayDetails() throws Exception {
        Product product = new Product();
        product.setName("상품01");
        doReturn(product).when(catalogService).findById("p01");

        mockMvc.perform(
                        get("/catalog/display-details")
                                .param("productId", "p01")
                )
                .andExpect(content().string(containsString("상품01")))
        ;
    }
}
