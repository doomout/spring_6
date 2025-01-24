package com.example.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.shopping.exception.StockShortageException;
import com.example.shopping.input.CartInput;
import com.example.shopping.input.OrderInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shopping.entity.Order;
import com.example.shopping.enumeration.PaymentMethod;
import com.example.shopping.input.CartItemInput;
import com.example.shopping.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 실습 편의 상 장바구니의 내용은 고정값으로 합니다.
    private CartInput dummyCartInput() {
        List<CartItemInput> cartItemInputs = new ArrayList<>();

        CartItemInput keshigom = new CartItemInput();
        keshigom.setProductId("p01");
        keshigom.setProductName("지우개");
        keshigom.setProductPrice(100);
        keshigom.setQuantity(4);
        cartItemInputs.add(keshigom);

        CartItemInput note = new CartItemInput();
        note.setProductId("p02");
        note.setProductName("노트");
        note.setProductPrice(200);
        note.setQuantity(5);
        cartItemInputs.add(note);

        CartInput cartInput = new CartInput();
        cartInput.setCartItemInputs(cartItemInputs);
        return cartInput;
    }

    @GetMapping("/display-form")
    public String displayForm(Model model) {
        OrderInput orderInput = new OrderInput();
        orderInput.setPaymentMethod(PaymentMethod.BANK);
        model.addAttribute("orderInput", orderInput);
        return "order/orderForm";
    }

    @PostMapping("/validate-input")
    public String validateInput(
            //@Validated 를 붙이면 핸들러 메서드가 호출 되기전에 입력 검사가 실시된다.
            @Validated OrderInput orderInput, BindingResult bindingResult, Model model) {
        //에러를 발견하면~
        if(bindingResult.hasErrors()) {
            return "order/orderForm"; //이 페이지로 이동
        }
        CartInput cartInput = dummyCartInput();
        model.addAttribute("cartInput", cartInput);
        return "order/orderConfirmation";
    }

    //파라미터에 따라 다른 기능을 하도록 수정 correct 에는 주문 내용 변경으로
    @PostMapping(value = "/place-order", params = "correct")
    public String correctInput(@Validated OrderInput orderInput, Model model) {
        return "order/orderForm";
    }

    //파라미터에 따라 다른 기능을 하도록 수정 order 에는 주문 확정으로
    @PostMapping(value = "/place-order", params = "order")
    public String order(@Validated OrderInput orderInput, Model model) {
        CartInput cartInput = dummyCartInput();
        Order order = orderService.placeOrder(orderInput, cartInput);
        model.addAttribute("order", order);
        return "order/orderCompletion";
    }
    
    //예외 처리
    @ExceptionHandler(StockShortageException.class)
    public String displayStockShortagePage() {
        return "order/stockShortage";
    }
}
