package com.example.shopping;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.shopping.input.OrderInput;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.input.CartInput;
import com.example.shopping.input.CartItemInput;
import com.example.shopping.service.OrderService;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@Configuration
@ComponentScan
public class ShoppingApplication {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("schema.sql", "data.sql")
                .setType(EmbeddedDatabaseType.H2).build();
        return dataSource;
    }

    //JdbcTemplate 사용
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {

        @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(ShoppingApplication.class);
        // JdbcTemplate이 SQL을 로그 출력하도록 설정
        ((Logger) LoggerFactory.getLogger(JdbcTemplate.class.getName())).setLevel(Level.DEBUG);
        OrderService orderService = context.getBean(OrderService.class);

        OrderInput orderInput = new OrderInput();
        orderInput.setName("김철수");
        orderInput.setAddress("서울시");
        orderInput.setPhone("090-0000-0000");
        orderInput.setEmailAddress("taro@example.com");
        orderInput.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        List<CartItemInput> cartItemInputs = new ArrayList<>();

        CartItemInput keshigom = new CartItemInput();
        keshigom.setProductId("p01");
        keshigom.setProductName("지우개");
        keshigom.setProductPrice(100);
        keshigom.setQuantity(3);
        cartItemInputs.add(keshigom);

        CartItemInput note = new CartItemInput();
        note.setProductId("p02");
        note.setProductName("노트");
        note.setProductPrice(200);
        note.setQuantity(4);
        cartItemInputs.add(note);

        CartInput cartInput = new CartInput();
        cartInput.setCartItemInputs(cartItemInputs);

        Order order = orderService.placeOrder(orderInput, cartInput);

        System.out.println("주문 확정 처리를 완료했습니다. 주문 ID=" + order.getId());
    }
}

/* 로그 + 실행 결과
22:58:35.114 [main] INFO org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory -- Starting embedded database: url='jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'
22:58:35.851 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL update
22:58:35.853 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [INSERT INTO t_order values (?, ?, ?, ?, ?, ?, ?, ?)]
22:58:35.911 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL query
22:58:35.911 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [SELECT * FROM t_product WHERE id=?]
22:58:35.928 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL update
22:58:35.928 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [UPDATE t_product SET name=?, price=?, stock=? WHERE id=?]
22:58:35.934 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL update
22:58:35.934 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [INSERT INTO t_order_item VALUES (?, ?, ?, ?, ?)]
22:58:35.936 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL query
22:58:35.936 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [SELECT * FROM t_product WHERE id=?]
22:58:35.937 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL update
22:58:35.937 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [UPDATE t_product SET name=?, price=?, stock=? WHERE id=?]
22:58:35.940 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL update
22:58:35.940 [main] DEBUG org.springframework.jdbc.core.JdbcTemplate -- Executing prepared SQL statement [INSERT INTO t_order_item VALUES (?, ?, ?, ?, ?)]
주문 확정 처리를 완료했습니다. 주문 ID=97266e0d-fc07-4a3d-ba15-31396408b2a6
*/