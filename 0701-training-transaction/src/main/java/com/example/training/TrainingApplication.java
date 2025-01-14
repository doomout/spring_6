package com.example.training;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.training.entity.Reservation;
import com.example.training.input.ReservationInput;
import com.example.training.service.ReservationService;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@Configuration // 스프링 설정 클래스를 나타냄
@ComponentScan // 컴포넌트 스캔 활성화 (같은 패키지 및 하위 패키지에서 @Component, @Service, @Repository 등을 검색)
@EnableTransactionManagement  // 트랜잭션 관리를 활성화
public class TrainingApplication {

    // DataSource 빈 정의: H2 임베디드 데이터베이스를 사용
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                .addScripts("schema.sql", "data.sql") // 데이터베이스 초기화 스크립트 추가
                .setType(EmbeddedDatabaseType.H2) // H2 데이터베이스 유형 지정
                .build();  // 데이터베이스 생성
        return dataSource;
    }

    // JdbcTemplate 빈 정의: DataSource를 활용하여 데이터베이스 작업을 간소화
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // 트랜잭션 매니저 빈 정의: 데이터베이스 트랜잭션 관리
    @Bean
    public JdbcTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    public static void main(String[] args) {
        @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(TrainingApplication.class);

        // 트랜잭션 제어 로그를 출력하도록 설정
        ((Logger) LoggerFactory.getLogger(JdbcTransactionManager.class)).setLevel(Level.DEBUG);

        // ReservationService 빈을 컨텍스트에서 가져옴
        ReservationService reservationService = context.getBean(ReservationService.class);

        // 예약 입력 데이터 생성
        ReservationInput reservationInput = new ReservationInput();
        reservationInput.setName("김철수");
        reservationInput.setPhone("010-0000-0000");
        reservationInput.setEmailAddress("taro@example.com");
        reservationInput.setStudentTypeCode("FREELANCE");
        reservationInput.setTrainingId("t01");

        // 예약 요청: 서비스 계층을 통해 예약 수행
        Reservation reservation = reservationService.reserve(reservationInput);

        // 예약 완료 메시지 출력
        System.out.println("신청을 마쳤습니다. 신청 ID=" + reservation.getId());
    }

}

