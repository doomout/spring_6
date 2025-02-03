package com.example.shopping;

import com.example.shopping.entity.Product;
import com.example.shopping.input.ProductMaintenanceInput;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 실행 순서 지정
public class RestClientTest {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080/api/products";
    private static URI location;

    @Test
    @Order(1) // 상품 생성
    public void testCreateProduct() {
        // 상품 데이터를 담을 객체 생성
        ProductMaintenanceInput productMaintenanceInput = new ProductMaintenanceInput();
        productMaintenanceInput.setName("문서파쇄기"); // 상품명 설정
        productMaintenanceInput.setPrice(3500); // 가격 설정
        productMaintenanceInput.setStock(11); // 재고 수량 설정

        location = restTemplate.postForLocation(BASE_URL, productMaintenanceInput);
        System.out.println("1. 상품이 생성되었습니다: " + location);
    }

    @Test
    @Order(2) //상품 조회
    public void testGetProduct() {
        Assertions.assertNotNull(location, "상품이 먼저 생성되어야 합니다."); // 상품이 없으면 실패
        Product product = restTemplate.getForObject(location, Product.class);
        System.out.println("2. 가져온 상품 이름: " + product.getName());
    }

    @Test
    @Order(3) //상품 수정
    public void testUpdateProduct() {
        Assertions.assertNotNull(location, "상품이 먼저 생성되어야 합니다.");

        ProductMaintenanceInput updatedProduct = new ProductMaintenanceInput();
        updatedProduct.setName("문서파쇄기(신형)");

        restTemplate.put(location, updatedProduct);
        System.out.println("3. 상품 정보가 수정되었습니다.(" + updatedProduct.getName() +")");
    }

    @Test
    @Order(4) // 상품 삭제
    public void testDeleteProduct() {
        Assertions.assertNotNull(location, "상품이 먼저 생성되어야 합니다.");

        restTemplate.delete(location);
        System.out.println("4. 상품이 삭제되었습니다.");
    }
}
