package com.example.training.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.example.training.entity.Training;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("TrainingAdminRestControllerIntegrationTest.sql")
@Sql(value = "clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TrainingAdminRestControllerIntegrationTestApServer {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void test_getTrainings() {
        ResponseEntity<Training[]> responseEntity = testRestTemplate.getForEntity("/api/trainings", Training[].class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Training[] trainings = responseEntity.getBody();
        Assertions.assertThat(trainings.length).isEqualTo(3);
        Assertions.assertThat(trainings[0].getTitle()).isEqualTo("비즈니스 예절 교육");
        Assertions.assertThat(trainings[1].getTitle()).isEqualTo("자바 교육");
    }

    @Test
    void test_getTraining() {
        ResponseEntity<Training> responseEntity = testRestTemplate.getForEntity("/api/trainings/{id}", Training.class, "t01");
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Training training = responseEntity.getBody();
        Assertions.assertThat(training.getTitle()).isEqualTo("비즈니스 예절 교육");
    }
}