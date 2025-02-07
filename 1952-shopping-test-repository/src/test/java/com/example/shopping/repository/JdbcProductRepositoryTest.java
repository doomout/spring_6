package com.example.shopping.repository;

import com.example.shopping.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j // 로그 사용을 위한 Lombok 어노테이션
@JdbcTest
@Sql("JdbcProductRepositoryTest.sql")
public class JdbcProductRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new JdbcProductRepository(jdbcTemplate);
    }

    //최저 획득 건수가 기대한 만큼 나오는지 확인
    @Test
    void test_selectAll() {
        List<Product> products = productRepository.selectAll();

        // 로그를 활용한 중간 과정 출력
        log.info("=== 조회된 상품 목록 ===");
        products.forEach(product ->
                log.info("상품명: {}, 가격: {}, 재고: {}", product.getName(), product.getPrice(), product.getStock())
        );

        assertThat(products.size()).isEqualTo(5);
    }
}
