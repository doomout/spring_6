package com.example.shopping.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.OrderItem;

//DataSource -> jdbcTemplate 로 변환
@Repository
public class JdbcOrderItemRepository implements OrderItemRepository {
    //private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

//    public JdbcOrderItemRepository(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
    public JdbcOrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
//    dataSource 버전
//    @Override
//    public void insert(OrderItem orderItem) {
//        try (
//            Connection con = dataSource.getConnection();
//            PreparedStatement stmt
//                = con.prepareStatement("INSERT INTO t_order_item VALUES (?, ?, ?, ?, ?)");
//        ) {
//            stmt.setString(1, orderItem.getId());
//            stmt.setString(2, orderItem.getOrderId());
//            stmt.setString(3, orderItem.getProductId());
//            stmt.setInt(4, orderItem.getPriceAtOrder());
//            stmt.setInt(5, orderItem.getQuantity());
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            throw new RuntimeException("INSERT 실패", ex);
//        }
//    }
    @Override
    public void insert(OrderItem orderItem) {
        jdbcTemplate.update("INSERT INTO t_order_item VALUES (?, ?, ?, ?, ?)",
                orderItem.getId(),
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getPriceAtOrder(),
                orderItem.getQuantity()
        );
    }
}
