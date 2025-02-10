package com.example.shopping.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.shopping.entity.Order;
import com.example.shopping.input.OrderInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.input.CartInput;
import com.example.shopping.input.CartItemInput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest // SpringBoot 테스트 환경에서 실행
@AutoConfigureMockMvc // MockMvc 자동 설정
@Transactional // 각 테스트가 끝난 후 DB 트랜잭션 롤백
@Sql("OrderControllerIntegrationTest.sql") // 테스트 실행 전에 실행할 SQL 스크립트 지정
class OrderControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc; // 웹 요청을 테스트하는 MockMvc 객체

    @Autowired
    JdbcTemplate jdbcTemplate; // DB 데이터 검증을 위한 JdbcTemplate

    /**
     * 주문을 정상적으로 생성할 수 있는지 테스트
     */
    @Test
    void test_order() throws Exception {
        // 주문 정보 생성
        OrderInput orderInput = new OrderInput();
        orderInput.setName("김철수");
        orderInput.setAddress("서울시");
        orderInput.setPhone("010-0000-0000");
        orderInput.setEmailAddress("taro@example.com");
        orderInput.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        // 장바구니에 담을 상품 리스트 생성
        List<CartItemInput> cartItemInputs = new ArrayList<>();

        CartItemInput keshigom = new CartItemInput(); // 상품 1: 지우개
        keshigom.setProductId("p01");
        keshigom.setProductName("지우개");
        keshigom.setProductPrice(100);
        keshigom.setQuantity(3);
        cartItemInputs.add(keshigom);

        CartItemInput note = new CartItemInput(); // 상품 2: 노트
        note.setProductId("p02");
        note.setProductName("노트");
        note.setProductPrice(200);
        note.setQuantity(4);
        cartItemInputs.add(note);

        // 장바구니 입력값 설정
        CartInput cartInput = new CartInput();
        cartInput.setCartItemInputs(cartItemInputs);

        // 세션에 주문 정보 저장
        OrderSession orderSession = new OrderSession();
        orderSession.setOrderInput(orderInput);
        orderSession.setCartInput(cartInput);

        // 주문 요청을 보내고 결과를 저장
        MvcResult mvcResult = mockMvc.perform(
                        post("/order/place-order") // POST 요청 전송
                                .param("order", "") // 파라미터 설정 (실제 사용되지 않음)
                                .sessionAttr("scopedTarget.orderSession", orderSession) // 세션에 주문 정보 추가
                )
                .andExpect(redirectedUrl("/order/display-completion")) // 주문 완료 페이지로 리디렉트되는지 확인
                .andReturn();

        // Flash 속성에서 Order 객체 가져오기
        Order order = (Order) mvcResult.getFlashMap().get("order");

        // DB에서 주문 데이터 조회
        Map<String, Object> orderMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE id=?", order.getId());

        // 주문 정보 검증
        assertThat(orderMap.get("customer_name")).isEqualTo("김철수");
        assertThat(orderMap.get("customer_phone")).isEqualTo("010-0000-0000");
        assertThat(orderMap.get("customer_address")).isEqualTo("서울시");
        assertThat(orderMap.get("customer_email_address")).isEqualTo("taro@example.com");
        assertThat(orderMap.get("payment_method")).isEqualTo(PaymentMethod.CONVENIENCE_STORE.toString());
        assertThat(orderMap.get("billing_amount")).isEqualTo(1210); // 총 금액 1210원
        assertThat(orderMap.get("order_date_time")).isNotNull(); // 주문 시간이 기록되었는지 확인

        // 주문 상세 정보 개수 검증 (2개 상품)
        int orderItemCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM t_order_item WHERE order_id=?", Integer.class, order.getId());
        assertThat(orderItemCount).isEqualTo(2);

        // 재고가 정상적으로 감소했는지 확인
        int p01Stock = jdbcTemplate.queryForObject("SELECT stock FROM t_product WHERE id=?", Integer.class, "p01");
        int p02Stock = jdbcTemplate.queryForObject("SELECT stock FROM t_product WHERE id=?", Integer.class, "p02");
        assertThat(p01Stock).isEqualTo(7);  // 원래 10개 → 3개 주문 → 7개 남음
        assertThat(p02Stock).isEqualTo(16); // 원래 20개 → 4개 주문 → 16개 남음
    }

    /**
     * 재고가 부족할 때 적절한 오류 메시지가 표시되는지 테스트
     */
    @Test
    void test_order_재고부족() throws Exception {
        // 주문 정보 생성 (이름, 주소 정보 없이 결제 방법만 설정)
        OrderInput orderInput = new OrderInput();
        orderInput.setPaymentMethod(PaymentMethod.CONVENIENCE_STORE);

        // 장바구니에 담을 상품 리스트 생성
        List<CartItemInput> cartItemInputs = new ArrayList<>();
        CartItemInput cartItemInput = new CartItemInput();
        cartItemInput.setProductId("p01"); // 재고가 부족한 상품 ID
        cartItemInput.setProductPrice(100);
        cartItemInput.setQuantity(11); // 11개 주문 (재고보다 많음)
        cartItemInputs.add(cartItemInput);

        // 장바구니 입력값 설정
        CartInput cartInput = new CartInput();
        cartInput.setCartItemInputs(cartItemInputs);

        // 세션에 주문 정보 저장
        OrderSession orderSession = new OrderSession();
        orderSession.setOrderInput(orderInput);
        orderSession.setCartInput(cartInput);

        // 주문 요청을 보내고, 응답 내용이 "재고부족"을 포함하는지 확인
        mockMvc.perform(
                        post("/order/place-order")
                                .param("order", "")
                                .sessionAttr("scopedTarget.orderSession", orderSession)
                )
                .andExpect(content().string(containsString("재고부족"))); // 재고 부족 메시지가 화면에 표시되는지 확인
    }
}
