package com.example.training.service;

import java.util.List;

import com.example.training.repository.TrainingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.training.entity.Training;

//테스트를 작성하기 위한 클래스
class TrainingServiceImplTest {
    @Test //이 어노테이션은 JUnit에게 이 메서드가 테스트 메서드임을 알려줍니다.
    public void test_findAll() { //findAll() 메서드가 올바르게 동작하는지 확인하는 테스트를 수행합니다.
        // Mock 객체 생성
        TrainingRepository trainingRepository = new MockTrainingRepository();
        // Service 객체 생성
        TrainingService trainingService = new TrainingServiceImpl(trainingRepository);

        // findAll() 메서드 호출
        List<Training> trainings = trainingService.findAll();
        // 결과 검증
        Assertions.assertThat(trainings.size()).isEqualTo(10);

        /** 기존 코드
         TrainingService trainingService = null;

         @SuppressWarnings("null")
         List<Training> trainings = trainingService.findAll();
         // 결과 확인
         Assertions.assertThat(trainings.size()).isEqualTo(10);
         */
    }
}