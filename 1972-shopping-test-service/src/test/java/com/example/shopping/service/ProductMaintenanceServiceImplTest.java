package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.input.ProductMaintenanceInput;
import com.example.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

// Mockito를 사용한 JUnit 5 테스트 확장을 적용
@ExtendWith(MockitoExtension.class)
class ProductMaintenanceServiceImplTest {

    // @InjectMocks: Mock 객체들이 주입될 클래스
    @InjectMocks
    ProductMaintenanceServiceImpl productMaintenanceService;

    // @Mock: 가짜(Mock) 객체로 사용할 ProductRepository 선언
    @Mock
    ProductRepository productRepository;

    // update() 메서드가 정상적으로 동작하는지 검증하는 테스트
    @Test
    public void test_update() {
        // productRepository.update(any())가 호출되면 항상 true를 반환하도록 설정
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        doReturn(true).when(productRepository).update(productCaptor.capture());

        // 테스트용 입력 데이터 생성
        ProductMaintenanceInput productMaintenanceInput = new ProductMaintenanceInput();
        productMaintenanceInput.setId("p01");
        productMaintenanceInput.setName("pname01");
        productMaintenanceInput.setPrice(100);
        productMaintenanceInput.setStock(10);

        // 서비스의 update() 메서드를 실행 (예외가 발생하지 않으면 성공)
        productMaintenanceService.update(productMaintenanceInput);

        //메서드의 인수로 전달된 Product 객체의 필드 값이 올바른지 확인
        Product product = productCaptor.getValue();
        assertThat(product.getId()).isEqualTo("p01");
        assertThat(product.getName()).isEqualTo("pname01");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getStock()).isEqualTo(10);
    }

    // update() 실행 시 갱신이 실패했을 때 예외 발생 여부를 확인하는 테스트
    @Test
    public void test_update_갱신실패() {
        // productRepository.update(any())가 호출되면 false를 반환하도록 설정
        doReturn(false).when(productRepository).update(any());
        ProductMaintenanceInput productMaintenanceInput = new ProductMaintenanceInput();

        // update() 실행 시 OptimisticLockingFailureException 예외가 발생하는지 확인
        assertThatThrownBy(() -> {
            productMaintenanceService.update(productMaintenanceInput);
        }).isInstanceOf(OptimisticLockingFailureException.class);
    }

    // findAll() 메서드가 모든 상품 목록을 정상적으로 반환하는지 검증하는 테스트
    @Test
    void test_findAll() {
        // 가짜 상품 리스트 생성
        List<Product> products = new ArrayList<>();
        Product prod1 = new Product();
        prod1.setName("바인더");
        products.add(prod1);
        Product prod2 = new Product();
        prod2.setName("수정액");
        products.add(prod2);

        // productRepository.selectAll()가 호출되면 products 리스트를 반환하도록 설정
        doReturn(products).when(productRepository).selectAll();

        // findAll() 실행 후 결과 검증
        List<Product> actual = productMaintenanceService.findAll();
        assertThat(actual.size()).isEqualTo(2);  // 리스트 크기가 2인지 확인
    }

    //최소한 검색 결과가 예상대로 나오는지 확인
    @Test
    void test_findById() {
        Product prod = new Product();
        prod.setName("바인더");
        doReturn(prod).when(productRepository).selectById("p01");

        Product product = productMaintenanceService.findById("p01");
        assertThat(product.getName()).isEqualTo("바인더");
    }
}
