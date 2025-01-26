package com.example.shopping.controller;

import com.example.shopping.entity.Order;
import com.example.shopping.service.OrderMaintenanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//이 클래스가 REST API의 컨트롤러임을 나타냅니다.
//Spring MVC에서 JSON 형태의 데이터를 반환하는 API를 쉽게 작성할 수 있게 해주는 어노테이션입니다.
@RestController
//이 클래스의 모든 메서드가 /api/orders 경로를 기본으로 하도록 설정합니다.
@RequestMapping("/api/orders")
//주문 데이터를 처리하는 비즈니스 로직을 담당하는 서비스 클래스
public class OrderMaintenanceRestController {
    private final OrderMaintenanceService orderMaintenanceService;

    //OrderMaintenanceService를 생성자를 통해 주입받아 의존성을 관리합니다.
    public OrderMaintenanceRestController(OrderMaintenanceService orderMaintenanceService) {
        this.orderMaintenanceService = orderMaintenanceService;
    }
    // HTTP GET 요청을 처리하는 메서드임을 나타냅니다.
    // api/orders로 요청이 들어오면 이 메서드가 실행됩니다.
    @GetMapping
    public List<Order> getOrders() {
        //호출을 통해 모든 주문 데이터를 가져옵니다.
        return orderMaintenanceService.findAll();
    }

    //HTTP GET 요청 중 /api/orders/{id} 경로를 처리합니다.
    //{id}는 경로 변수로, 요청 시 주문 ID를 포함합니다. 예: /api/orders/123
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) { //@PathVariable: 경로 변수 {id}를 메서드의 매개변수 id로 매핑합니다.
        Order order = orderMaintenanceService.findById(id);
        return order;
    }
}
