package com.example.shopping.controller;

import com.example.shopping.entity.Product;
import com.example.shopping.service.ProductMaintenanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        Product product = productMaintenanceService.findById(id);
        return product;
    }
}
