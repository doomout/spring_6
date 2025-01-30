package com.example.shopping.repository;

import com.example.shopping.entity.Product;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.OrderItem;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcOrderItemRepository implements OrderItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OrderItem selectById(String id) {
        return jdbcTemplate.queryForObject("""
            SELECT
              i.id AS i_id,
              i.order_id AS i_order_id,
              i.product_id AS i_product_id,
              i.price_at_order AS i_price_at_order,
              i.quantity AS i_quantity,
              p.id AS p_id,
              p.name AS p_name,
              p.price AS p_price,
              p.stock AS p_stock
            FROM
              t_order_item i
              LEFT OUTER JOIN t_product p 
              ON i.product_id = p.id
            WHERE
              i.id = ?""", new DataClassRowMapper<>(OrderItem.class), id);
    }

    //제대로 객체에 맵핑되도록 수정합시다. RowMapper 인터페이스를 구현한 클래스를 작성해야 합니다.
    static class OrderItemRowMapper implements RowMapper<OrderItem> {
        @Override
        public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getString("i_id"));
            orderItem.setOrderId(rs.getString("i_order_id"));
            orderItem.setProductId(rs.getString("i_product_id"));
            orderItem.setPriceAtOrder(rs.getInt("i_price_at_order"));
            orderItem.setQuantity(rs.getInt("i_quantity"));

            Product product = new Product();
            product.setId(rs.getString("p_id"));
            product.setName(rs.getString("p_name"));
            product.setPrice(rs.getInt("p_price"));
            product.setStock(rs.getInt("p_stock"));

            orderItem.setProduct(product);

            return orderItem;
        }
    }
}
