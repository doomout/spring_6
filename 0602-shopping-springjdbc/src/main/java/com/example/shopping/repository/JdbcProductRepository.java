package com.example.shopping.repository;


import com.example.shopping.entity.Product;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//DataSource -> jdbcTemplate 으로 변환
@Repository
public class JdbcProductRepository implements ProductRepository {
    // JdbcTemplate을 사용하기 위한 멤버 변수 선언
    private final JdbcTemplate jdbcTemplate;

    // 생성자를 통해 JdbcTemplate 객체를 주입받음
    public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 상품 ID로 상품 정보를 조회하는 메서드
    @Override
    public Product selectById(String id) {
        // queryForObject: 단일 행 결과를 객체로 매핑
        return jdbcTemplate.queryForObject(
                // SQL 쿼리: 상품 ID로 상품 정보를 검색
                "SELECT * FROM t_product WHERE id=?",
                // DataClassRowMapper: SQL 결과를 Product 클래스 객체로 매핑
                new DataClassRowMapper<>(Product.class),
                id // 쿼리의 첫 번째 ?에 id 값이 바인딩됨
        );
    }

    @Override
    public boolean update(Product product) {
        // update: SQL 쿼리를 실행하고 영향을 받은 행의 개수를 반환
        int count = jdbcTemplate.update(
                "UPDATE t_product SET name=?, price=?, stock=? WHERE id=?",
                product.getName(), // 첫 번째 ?에 product의 이름 값 바인딩
                product.getPrice(), // 두 번째 ?에 product의 가격 값 바인딩
                product.getStock(), // 세 번째 ?에 product의 재고 값 바인딩
                product.getId() // 네 번째 ?에 product의 ID 값 바인딩
        );
        // 업데이트된 행이 없으면 false 반환
        if (count == 0) {
            return false;
        }
        // 업데이트 성공 시 true 반환
        return true;
    }
}