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

    //ID로 검색하여 Product 객체의 내용이 예상과 일치하는지 확인
    @Test
    void test_selectById() {
        Product product = productRepository.selectById("p01");

        // 로그 출력 (INFO 레벨)
        log.info("=== 조회된 상품 정보 ===");
        log.info("ID: {}", product.getId());
        log.info("상품명: {}", product.getName());
        log.info("가격: {}", product.getPrice());
        log.info("재고: {}", product.getStock());

        //검증
        assertThat(product.getName()).isEqualTo("지우개");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getStock()).isEqualTo(10);
    }
}
