package com.example.shopping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.input.CartInput;
import com.example.shopping.input.CartItemInput;
import com.example.shopping.input.OrderInput;
import com.example.shopping.service.OrderService;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan
public class ShoppingApplication {
    // 코드 추가
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("schema.sql", "data.sql")
                .setType(EmbeddedDatabaseType.H2).build();
        return dataSource;
    }


    public static void main(String[] args) {
        @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(ShoppingApplication.class);
        OrderService orderService = context.getBean(OrderService.class);

        OrderInput orderInput = new OrderInput();
        orderInput.setName("김철수");
        orderInput.setAddress("서울시");
        orderInput.setPhone("010-0000-0000");
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

/* 실행 결과
[main] INFO org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory
-- Starting embedded database: url='jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'
주문 확정 처리를 완료했습니다. 주문 ID=5efaff2c-d27f-4827-96c1-2399c1761b09
*/