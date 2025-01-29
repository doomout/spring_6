package com.example.calculate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculateController {
    //전역 변수는 다중 스레드에서 에러 발생
    //private int total;

	@GetMapping("/calculate-price")
	public String calculatePrice(@RequestParam int a, @RequestParam int b, @RequestParam int c, Model model) {
        //지역 변수로 안전하게 에러 제거
        int total = a + b + c;
        System.out.println(total);
        int price = (int)(total * 1.1);
        model.addAttribute("price", price);
        return "price";
	}
}
