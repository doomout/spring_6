package com.example.shopping.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.shopping.exception.StockShortageException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.input.CartInput;
import com.example.shopping.input.CartItemInput;
import com.example.shopping.input.OrderInput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
@Sql("OrderServiceTest.sql")
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test_placeOrder() {
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

        //발급받은 주문 ID를 사용하여 주문 데이터를 검색
        Map<String, Object> orderMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE id=?", order.getId());

        //검색한 주문 데이터의 고객 데이터가 예상대로인가?
        assertThat(orderMap.get("customer_name")).isEqualTo("김철수");
        assertThat(orderMap.get("customer_phone")).isEqualTo("010-0000-0000");
        assertThat(orderMap.get("customer_address")).isEqualTo("서울시");
        assertThat(orderMap.get("customer_email_address")).isEqualTo("taro@example.com");
        assertThat(orderMap.get("payment_method")).isEqualTo(PaymentMethod.CONVENIENCE_STORE.toString());
        assertThat(orderMap.get("billing_amount")).isEqualTo(1210);
        assertThat(orderMap.get("order_date_time")).isNotNull();

        //발급된 주문 ID에 연결된 주문 상세 내역 데이터의 개수가 예상대로인가?
        int orderItemCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_order_item WHERE order_id=?", Integer.class, order.getId());
        assertThat(orderItemCount).isEqualTo(2);

        //주문한 상품의 재고 수량이 예상대로 변경되었는가?
        int p01Stock = jdbcTemplate.queryForObject("SELECT stock FROM t_product WHERE id=?", Integer.class, "p01");
        int p02Stock = jdbcTemplate.queryForObject("SELECT stock FROM t_product WHERE id=?", Integer.class, "p02");
        assertThat(p01Stock).isEqualTo(7);
        assertThat(p02Stock).isEqualTo(16);
    }

    //재고가 부족할 때 예상한 예외가 던져지는지 확인하는 테스트 메서드
    @Test
    void test_placeOrder_재고부족() {
        OrderInput orderInput = new OrderInput();
        orderInput.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        //장바구니 생성 및 상품 추가
        List<CartItemInput> cartItemInputs = new ArrayList<>();
        CartItemInput cartItemInput = new CartItemInput();
        cartItemInput.setProductId("p01");
        cartItemInput.setProductPrice(100);
        cartItemInput.setQuantity(11); //재고 부족을 발생시키기 위해 일부러 11개 주문
        cartItemInputs.add(cartItemInput); //장바구니에 상품 추가

        //장바구니 정보를 저장
        CartInput cartInput = new CartInput();
        cartInput.setCartItemInputs(cartItemInputs);

        //주문할 때 StockShortageException 예외가 발생하는지 검증
        assertThatThrownBy(() -> {
            orderService.placeOrder(orderInput, cartInput);
        }).isInstanceOf(StockShortageException.class);
    }

}
