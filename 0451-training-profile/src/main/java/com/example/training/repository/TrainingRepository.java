package com.example.training.repository;

import java.util.List;

import com.example.training.entity.Training;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//프로파일을 지정하지 않았다.
public interface TrainingRepository {
    List<Training> selectAll();
}
