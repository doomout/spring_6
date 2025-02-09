package com.example.shopping.service;

import com.example.shopping.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//Service, Repository 통합 테스트기에 스프링 MVC 구동을 위한 웹 관련 설정이 필요 없다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional //@SpringBootTest 에는 트렌젝션이 미포함이라 명시적으로 설정해야 한다.
@Sql("CatalogServiceTest.sql") //테스트를 위한 데이터 인서트
public class CatalogServiceTest {
    @Autowired
    CatalogService catalogService;

    //최소한 검색 결과가 예상대로 나오는지 확인
    @Test
    void test_findAll() {
        List<Product> products = catalogService.findAll();
        assertThat(products.size()).isEqualTo(5);
    }

    //ID로 검색하여 Product 객체의 내용이 예상과 일치하는지 확인
    @Test
    void test_findById() {
        Product product = catalogService.findById("p01");
        assertThat(product.getName()).isEqualTo("지우개");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getStock()).isEqualTo(10);
    }
}
