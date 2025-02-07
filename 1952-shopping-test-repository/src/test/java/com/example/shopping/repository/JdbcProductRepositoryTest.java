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
import java.util.Map;

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

    //해당 ID의 데이터를 업데이트하고 t_product 테이블의 레코드가 예상대로 변경되는지 확인
    @Test
    void test_update() {
        Product training = new Product();
        training.setId("p01");
        training.setName("엽서");
        training.setPrice(120);
        training.setStock(5);

        log.info("업데이트할 제품 데이터: {}", training);

        boolean result = productRepository.update(training);

        // 업데이트 결과 확인 로그
        log.info("업데이트 결과: {}", result);

        assertThat(result).isEqualTo(true);

        Map<String, Object> trainingMap = jdbcTemplate.queryForMap(
                "SELECT * FROM t_product WHERE id=?", "p01");

        // DB에서 데이터를 조회하여 로그로 출력
        log.info("DB에서 조회한 데이터: {}", trainingMap);

        assertThat(trainingMap.get("name")).isEqualTo("엽서");
        assertThat(trainingMap.get("price")).isEqualTo(120);
        assertThat(trainingMap.get("stock")).isEqualTo(5);
    }
}
