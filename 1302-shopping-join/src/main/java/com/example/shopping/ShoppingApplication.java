package com.example.shopping;


import com.example.shopping.entity.Order;
import com.example.shopping.entity.OrderItem;
import com.example.shopping.repository.OrderItemRepository;
import com.example.shopping.repository.OrderRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

//수정했으면 TrainingApplication 클래스의 main 메서드 안에서 selectById 메서드를 호출하여 객체의 내용을 확인합시다.
@SpringBootApplication
public class ShoppingApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ShoppingApplication.class, args);
        OrderItemRepository orderItemRepository = context.getBean(OrderItemRepository.class);
        OrderItem orderItem = orderItemRepository.selectById("i01");

        System.out.println("------------- OrderItem의 내용 -------------");
        System.out.println(orderItem.getPriceAtOrder());
        System.out.println(orderItem.getProduct());

        OrderRepository orderRepository = context.getBean(OrderRepository.class);
        Order order = orderRepository.selectById("o01");

        System.out.println("------------- Order의 내용 -------------");
        System.out.println(order.getCustomerName());
        for (OrderItem i : order.getOrderItems()) {
            System.out.println(i.getPriceAtOrder());
        }
    }
}

