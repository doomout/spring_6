package com.example.training;

import java.util.List;

import com.example.training.entity.Training;
import com.example.training.repository.JdbcTrainingRepository;
import com.example.training.repository.TrainingRepository;
import com.example.training.service.TrainingService;
import com.example.training.service.TrainingServiceImpl;

public class TrainingApplication {
    public static void main(String[] args) {
        TrainingRepository trainingRepository = new JdbcTrainingRepository();
        TrainingService trainingService = new TrainingServiceImpl(trainingRepository);

        List<Training> trainings = trainingService.findAll();
        for (Training training : trainings) {
            System.out.println(training.getTitle());
        }
    }
}

/* 실행 결과
데이터베이스에서 데이터를 가져옵니다.
title_0
title_1
title_2
title_3
title_4
title_5
title_6
title_7
title_8
title_9
*/