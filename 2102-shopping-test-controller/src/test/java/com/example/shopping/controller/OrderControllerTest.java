package com.example.shopping.controller;

import com.example.shopping.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
}
