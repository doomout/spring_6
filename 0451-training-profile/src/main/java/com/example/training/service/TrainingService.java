package com.example.training.service;

import java.util.List;

import com.example.training.entity.Training;
import org.springframework.stereotype.Service;

//프로파일을 지정하지 않았다.
public interface TrainingService {
    List<Training> findAll();
}
