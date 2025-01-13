package com.example.shopping.repository;

import com.example.shopping.entity.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// DataSource -> jdbcTemplate 로 변환
// JdbcTemplate은 Spring에서 제공하는 클래스로, 반복되는 JDBC 작업(연결, 명령 실행, 예외 처리)을 단순화해 줌.
// 기존 DataSource 방식에서는 데이터베이스 연결과 자원 관리를 직접 처리해야 했음.
// JdbcTemplate은 내부적으로 자원 관리와 예외 처리를 대신 수행하여 코드가 간결해짐.
@Repository // @Repository 애너테이션을 사용하여 Spring의 컴포넌트 스캔에서 이 클래스를 빈으로 등록
public class JdbcOrderItemRepository implements OrderItemRepository {
    // JdbcTemplate을 사용하기 위한 멤버 변수 선언
    private final JdbcTemplate jdbcTemplate;

    // 생성자를 통해 JdbcTemplate을 주입받는다
    public JdbcOrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // OrderItem 데이터를 데이터베이스에 삽입하는 메서드
    @Override
    public void insert(OrderItem orderItem) {
        jdbcTemplate.update("INSERT INTO t_order_item VALUES (?, ?, ?, ?, ?)",
                orderItem.getId(), // 첫 번째 ? -> OrderItem의 ID
                orderItem.getOrderId(), // 두 번째 ? -> 관련된 Order의 ID
                orderItem.getProductId(), // 세 번째 ? -> Product의 ID
                orderItem.getPriceAtOrder(), // 네 번째 ? -> 주문 당시의 가격
                orderItem.getQuantity()  // 다섯 번째 ? -> 주문한 수량
        );
    }
}
