package com.example.shopping.ui;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.codeborne.selenide.Configuration;

import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

// Spring Boot의 통합 테스트를 위한 설정
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderUiTest {

    // Spring Boot가 실행될 때 랜덤으로 할당되는 포트를 가져옴
    @Value("${local.server.port}")
    int randomPort;

    // 데이터베이스 연동을 위한 JdbcTemplate 주입
    @Autowired
    JdbcTemplate jdbcTemplate;

    // 각 테스트 실행 전에 수행할 설정 (테스트 대상 애플리케이션의 기본 URL 설정)
    @BeforeEach
    void setup() {
        Configuration.baseUrl = "http://localhost:" + randomPort;
    }

    // 주문 UI 테스트
    @Test
    @Sql("OrderUiTest.sql") // 테스트 실행 전 OrderUiTest.sql을 실행하여 초기 데이터 설정
    @Sql(value = "clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD) // 테스트 종료 후 clear.sql 실행 (DB 정리)
    void test_주문() {
        // 1. 상품 목록 페이지 열기
        open("/catalog/display-list");

        // 2. 특정 상품 클릭하여 상세 페이지로 이동
        $("a[href*=productId]").click();

        // 3. 수량 입력 필드에 '2' 입력
        $("input[name=quantity]").setValue("2");

        // 4. '장바구니에 추가' 버튼 클릭
        $("input[value=장바구니에추가]").click();

        // 5. '주문하기' 버튼 클릭
        $("input[value=주문하기]").click();

        // 6. 주문 정보 입력
        $("input[name=name]").setValue("김철수"); // 고객 이름 입력
        $("input[name=address]").setValue("서울시"); // 배송 주소 입력
        $("input[name=phone]").setValue("010-0000-0000"); // 전화번호 입력
        $("input[name=emailAddress]").setValue("taro@example.com"); // 이메일 입력

        // 7. 결제 방법 선택 (편의점 결제)
        $("input[name=paymentMethod]").selectRadio("CONVENIENCE_STORE");

        // 8. '주문 내용 확인' 버튼 클릭
        $("input[value=주문내용확인]").click();

        // 9. '주문 확정' 버튼 클릭
        $("input[value=주문확정]").click();

        // 10. 주문 완료 후 화면에서 주문 ID 추출
        String orderId = $("div span").text();

        // 11. 데이터베이스에서 해당 주문 정보를 조회하여 검증
        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE id=?", orderId);

        // 12. 저장된 주문의 고객 이름이 '김철수'인지 검증
        Assertions.assertThat(reservationMap.get("customer_name")).isEqualTo("김철수");
    }
}

