package com.example.shopping.repository;

import com.example.shopping.entity.OrderItem;
import com.example.shopping.enumeration.PaymentMethod;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository // 스프링이 이 클래스를 데이터 저장소로 인식하도록 지정
public class JdbcOrderRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate; // JDBC 연산을 수행하기 위한 JdbcTemplate 객체

    // 생성자를 통해 JdbcTemplate 객체를 주입받음
    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order selectById(String id) {

        return jdbcTemplate.queryForObject("""
            SELECT
              o.id AS o_id,
              o.order_date_time AS o_order_date_time,
              o.billing_amount AS o_billing_amount,
              o.customer_name AS o_customer_name,
              o.customer_address AS o_customer_address,
              o.customer_phone AS o_customer_phone,
              o.customer_email_address AS o_customer_email_address,
              o.payment_method AS o_payment_method,
              i.id AS i_id,
              i.order_id AS i_order_id,
              i.product_id AS i_product_id,
              i.price_at_order AS i_price_at_order,
              i.quantity AS i_quantity
            FROM
              t_order o
              LEFT OUTER JOIN t_order_item i 
              ON o.id = i.order_id
            WHERE
              o.id = ?""", new DataClassRowMapper<>(Order.class), id);
        // DataClassRowMapper<Order>를 사용하여 SQL 결과를 Order 객체로 변환
    }

    // ResultSet을 직접 다루며, 일대다 관계(주문 - 주문 항목)를 맵핑하기 위한 클래스
    static class OrderResultSetExtractor implements ResultSetExtractor<Order> {
        @Override
        public Order extractData(ResultSet rs) throws SQLException, DataAccessException {
            Order order = null; // Order 객체 초기화

            // 조회된 결과(ResultSet)를 순회
            while(rs.next()) {
                if (order == null) { // 첫 번째 행에서 Order 객체를 생성 및 초기화
                    order = new Order();
                    order.setOrderItems(new ArrayList<>()); // 주문 항목 리스트 초기화

                    order.setId(rs.getString("o_id")); // 주문 ID 설정
                    order.setOrderDateTime(rs.getObject("o_order_date_time", LocalDateTime.class));// 주문 날짜 설정
                    order.setBillingAmount(rs.getInt("o_billing_amount")); // 청구 금액 설정
                    order.setCustomerName(rs.getString("o_customer_name")); // 고객 이름 설정
                    order.setCustomerAddress(rs.getString("o_customer_address")); // 고객 주소 설정
                    order.setCustomerPhone(rs.getString("o_customer_phone")); // 고객 전화번호 설정
                    order.setCustomerEmailAddress(rs.getString("o_customer_email_address")); // 고객 이메일 설정
                    order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("o_payment_method"))); // 결제 방법 설정
                }

                // 주문 항목(OrderItem) 객체 생성 및 데이터 설정
                OrderItem orderItem = new OrderItem();
                orderItem.setId(rs.getString("i_id")); // 주문 항목 ID 설정
                orderItem.setOrderId(rs.getString("i_order_id")); // 주문 ID 설정 (외래키)
                orderItem.setProductId(rs.getString("i_product_id")); // 제품 ID 설정
                orderItem.setPriceAtOrder(rs.getInt("i_price_at_order")); // 주문 시 가격 설정
                orderItem.setQuantity(rs.getInt("i_quantity")); // 수량 설정

                // Order 객체의 주문 항목 리스트에 추가
                order.getOrderItems().add(orderItem);
            }
            return order; // 완성된 Order 객체 반환
        }
    }
}
