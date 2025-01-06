package com.example.training.service;

import java.util.List;

import com.example.training.entity.Training;
import com.example.training.repository.TrainingRepository;

// TrainingService 인터페이스의 구현체
public class TrainingServiceImpl implements TrainingService { //TrainingService라는 인터페이스를 구현하겠다는 선언

    //데이터를 처리할 때 사용하는 Repository 객체입니다.
    //final 키워드:  생성자를 통해 한 번 초기화되면 변경할 수 없습니다
    private final TrainingRepository trainingRepository;

    //생성자: 객체를 외부에서 주입받습니다.
    //TrainingServiceImpl 클래스는 특정 구현에 의존하지 않고, 유연성이 높아집니다.
    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        //생성자 매개변수로 전달받은 trainingRepository를 클래스의 필드로 저장합니다.
        this.trainingRepository = trainingRepository;
    }

    @Override //구현체가 제대로 인터페이스의 메서드를 재정의했는지 컴파일러가 확인하도록 합니다.
    public List<Training> findAll() { //trainingRepository의 selectAll() 메서드를 호출하여 데이터를 가져옵니다.
        //selectAll() 메서드가 데이터를 조회하고, 조회된 데이터를 findAll()의 결과로 반환합니다.
        return trainingRepository.selectAll();
    }
}
