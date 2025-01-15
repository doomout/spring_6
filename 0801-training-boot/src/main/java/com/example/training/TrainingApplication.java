package com.example.training;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

//기존 코드 -> 스프링 부트 설정으로 변경
//@Configuration
//@ComponentScan
//@EnableTransactionManagement
@SpringBootApplication
public class TrainingApplication {
//    @Bean
//    public DataSource dataSource() {
//        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
//                .addScripts("schema.sql", "data.sql")
//                .setType(EmbeddedDatabaseType.H2).build();
//        return dataSource;
//    }

//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

//    @Bean
//    public JdbcTransactionManager transactionManager(DataSource dataSource) {
//        return new JdbcTransactionManager(dataSource);
//    }

    public static void main(String[] args) {
        @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(TrainingApplication.class);
        // 트랜잭션 제어 로그를 출력하도록 설정
        ((Logger) LoggerFactory.getLogger(JdbcTransactionManager.class)).setLevel(Level.DEBUG);
        ReservationService reservationService = context.getBean(ReservationService.class);

        ReservationInput reservationInput = new ReservationInput();
        reservationInput.setName("김철수");
        reservationInput.setPhone("010-0000-0000");
        reservationInput.setEmailAddress("taro@example.com");
        reservationInput.setStudentTypeCode("FREELANCE");
        reservationInput.setTrainingId("t01");

        Reservation reservation = reservationService.reserve(reservationInput);

        System.out.println("신청을 마쳤습니다. 신청 ID=" + reservation.getId());
    }

}

/* 실행 결과
23:03:48.006 [main] INFO com.zaxxer.hikari.HikariDataSource -- HikariPool-1 - Starting...
23:03:48.281 [main] INFO com.zaxxer.hikari.pool.HikariPool -- HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:ec4ab2f6-ef06-4d9f-ba6a-b685a5fb5052 user=SA
23:03:48.284 [main] INFO com.zaxxer.hikari.HikariDataSource -- HikariPool-1 - Start completed.
23:03:48.660 [main] DEBUG org.springframework.jdbc.support.JdbcTransactionManager -- Creating new transaction with name [com.example.training.service.ReservationServiceImpl.reserve]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
23:03:48.664 [main] DEBUG org.springframework.jdbc.support.JdbcTransactionManager -- Acquired Connection [HikariProxyConnection@1245439653 wrapping conn0: url=jdbc:h2:mem:ec4ab2f6-ef06-4d9f-ba6a-b685a5fb5052 user=SA] for JDBC transaction
23:03:48.666 [main] DEBUG org.springframework.jdbc.support.JdbcTransactionManager -- Switching JDBC Connection [HikariProxyConnection@1245439653 wrapping conn0: url=jdbc:h2:mem:ec4ab2f6-ef06-4d9f-ba6a-b685a5fb5052 user=SA] to manual commit
23:03:48.707 [main] DEBUG org.springframework.jdbc.support.JdbcTransactionManager -- Initiating transaction commit
23:03:48.708 [main] DEBUG org.springframework.jdbc.support.JdbcTransactionManager -- Committing JDBC transaction on Connection [HikariProxyConnection@1245439653 wrapping conn0: url=jdbc:h2:mem:ec4ab2f6-ef06-4d9f-ba6a-b685a5fb5052 user=SA]
23:03:48.710 [main] DEBUG org.springframework.jdbc.support.JdbcTransactionManager -- Releasing JDBC Connection [HikariProxyConnection@1245439653 wrapping conn0: url=jdbc:h2:mem:ec4ab2f6-ef06-4d9f-ba6a-b685a5fb5052 user=SA] after transaction
신청을 마쳤습니다. 신청 ID=a1a240d4-7b89-4650-8844-54289b4ac189
* */