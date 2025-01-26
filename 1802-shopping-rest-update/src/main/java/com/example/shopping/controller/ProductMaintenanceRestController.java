package com.example.shopping.controller;

import java.net.URI;
import java.util.List;

import com.example.shopping.exception.DataNotFoundException;
import com.example.shopping.input.ProductMaintenanceInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.shopping.entity.Product;
import com.example.shopping.service.ProductMaintenanceService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        // 데이터가 없으면 예외 처리
        if (product == null) {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            throw new DataNotFoundException("데이터를 찾을 수 없습니다.  id=" + id);
        }
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

    //POST로 데이터를 등록한다
    @PostMapping
    public ResponseEntity registerProduct(
            @Validated @RequestBody ProductMaintenanceInput productMaintenanceInput) { //@Validated 입력 검사
        Product product = productMaintenanceService.register(productMaintenanceInput);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    //DELETE로 데이터를 삭제한다
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productMaintenanceService.delete(id);
    }

    //예외 처리 핸들러
    @ExceptionHandler({DataNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFound() {
        // 클라이언트에게 반환할 메시지를 작성
        String message = "요청하신 데이터를 찾을 수 없습니다.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}