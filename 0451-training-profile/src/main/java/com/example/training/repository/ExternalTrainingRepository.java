package com.example.training.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.example.training.entity.Training;

@Repository
@Profile("production") //프로파일을 설정하고 이름을 "production" 으로 지정했다.
public class ExternalTrainingRepository implements TrainingRepository {
    @Override
    public List<Training> selectAll() {
        System.out.println("외부 시스템에서 데이터를 가져옵니다.");
        // 외부에서 가져온다고 가정한다
        List<Training> trainings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Training training = new Training();
            training.setTitle("ex_title_" + i);
            trainings.add(training);
        }
        return trainings;
    }
}
