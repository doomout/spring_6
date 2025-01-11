package com.example.training;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.training.entity.Training;
import com.example.training.service.TrainingService;

@Configuration
@ComponentScan
public class TrainingApplication {

    public static void main(String[] args) {
        //production 모드로 지정했다.
        System.setProperty("spring.profiles.active", "production");
        @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(TrainingApplication.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        List<Training> trainings = trainingService.findAll();
        for (Training training : trainings) {
            System.out.println(training.getTitle());
        }
    }
}

/* 실행 결과
외부 시스템에서 데이터를 가져옵니다.
ex_title_0
ex_title_1
ex_title_2
ex_title_3
ex_title_4
ex_title_5
ex_title_6
ex_title_7
ex_title_8
ex_title_9
*/