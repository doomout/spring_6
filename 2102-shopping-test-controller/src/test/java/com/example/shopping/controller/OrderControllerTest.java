package com.example.shopping.controller;

import com.example.shopping.entity.Order;
import com.example.shopping.exception.StockShortageException;
import com.example.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//OrderController 클래스의 테스트 클래스를 생성하고,
//validateInput 메서드에 대해 입력 확인이 제대로 이루어지고
//원래의 입력 화면으로 돌아가는지 테스트하는 테스트 메서드를 생성합니다.
//세션 스코프의 Bean으로 사용하는 OrderSession 객체는
//Mock 객체를 사용해야 합니다(동작 지정은 필요 없음).

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderSession orderSession;

    //입력 확인 후 원래의 입력 화면으로 돌아가는지 테스트
    @Test
    void test_validateInput() throws Exception {
        mockMvc.perform(
                        post("/order/validate-input")
                )
                .andExpect(view().name("order/orderForm"))
                .andExpect(model().attributeHasFieldErrors("orderInput",
                        "name", "address", "phone", "emailAddress", "paymentMethod"))
        ;
    }

    //order 메서드(주문을 확정하는 핸들러 메서드)를 테스트
    @Test
    void test_order() throws Exception {
        Order order = new Order();
        doReturn(order).when(orderService).placeOrder(any(), any());

        mockMvc.perform(
                        post("/order/place-order")
                                .param("order", "")
                )
                .andExpect(flash().attribute("order", order))
                .andExpect(redirectedUrl("/order/display-completion"))
        ;
    }

    //주문을 확정할 때 재고가 부족할 경우,재고 부족 화면이 표시되는지 테스트
    @Test
    void test_order_재고부족() throws Exception {
        doThrow(StockShortageException.class).when(orderService).placeOrder(any(), any());

        mockMvc.perform(
                        post("/order/place-order")
                                .param("order", "")
                )
                .andExpect(content().string(containsString("재고부족")))
        ;
    }
}
