package com.example.shopping.controller;

import java.util.List;

import com.example.shopping.input.ProductMaintenanceInput;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.shopping.entity.Product;
import com.example.shopping.service.ProductMaintenanceService;

@RestController
@RequestMapping("/api/products")
public class ProductMaintenanceRestController {
    private final ProductMaintenanceService productMaintenanceService;

    public ProductMaintenanceRestController(ProductMaintenanceService productMaintenanceService) {
        this.productMaintenanceService = productMaintenanceService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return productMaintenanceService.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productMaintenanceService.findById(id);
    }

    //PUT으로 데이터를 갱신한다
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) //상태코드 204 사용
    public void updateProduct(@PathVariable String id,
                              @Validated @RequestBody ProductMaintenanceInput productMaintenanceInput) {  //@Validated 입력 검사
        productMaintenanceInput.setId(id);
        productMaintenanceService.update(productMaintenanceInput);
    }
}