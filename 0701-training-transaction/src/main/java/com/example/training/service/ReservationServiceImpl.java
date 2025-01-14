package com.example.training.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.entity.Training;
import com.example.training.exception.CapacityOverException;
import com.example.training.input.ReservationInput;
import com.example.training.repository.ReservationRepository;
import com.example.training.repository.StudentTypeRepository;
import com.example.training.repository.TrainingRepository;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스가 서비스 계층 역할을 한다는 것을 나타냄
@Transactional // 메서드 실행 중 트랜잭션 관리를 활성화
public class ReservationServiceImpl implements  ReservationService {
	// 필요한 리포지토리 의존성을 주입받음
	private final StudentTypeRepository studentTypeRepository; // 학생 유형 정보를 처리하는 리포지토리
	private final TrainingRepository trainingRepository; // 교육 과정 정보를 처리하는 리포지토리
	private final ReservationRepository reservationRepository; // 예약 정보를 처리하는 리포지토리

	// 생성자를 통해 의존성 주입
	public ReservationServiceImpl(StudentTypeRepository studentTypeRepository, TrainingRepository trainingRepository, ReservationRepository reservationRepository) {
		this.studentTypeRepository = studentTypeRepository;
		this.trainingRepository = trainingRepository;
		this.reservationRepository = reservationRepository;
	}

	@Override
	public Reservation reserve(ReservationInput reservationInput) {
		// 예약하려는 교육 과정 정보를 조회
		Training training = trainingRepository.selectById(reservationInput.getTrainingId());
		training.setReserved(training.getReserved() + 1); // 예약 수를 증가시킴

		// 정원 초과 여부를 확인
		if (training.getReserved() > training.getCapacity() ) {
			throw new CapacityOverException("정원초과"); // 정원을 초과하면 예외를 던짐
		}

		// 변경된 예약 수를 데이터베이스에 업데이트
		trainingRepository.update(training);

		// 학생 유형 정보를 조회
		StudentType studentType = studentTypeRepository.selectByCode(reservationInput.getStudentTypeCode());

		// 새로운 예약 객체 생성 및 입력 값 설정
		Reservation reservation = new Reservation();
		reservation.setId(UUID.randomUUID().toString()); // 고유한 예약 ID 생성
		reservation.setTrainingId(training.getId()); // 예약할 교육 과정 ID 설정
		reservation.setStudentTypeId(studentType.getId());  // 학생 유형 ID 설정
		reservation.setName(reservationInput.getName()); // 예약자 이름 설정
		reservation.setPhone(reservationInput.getPhone()); // 예약자 전화번호 설정
		reservation.setReservedDateTime(LocalDateTime.now()); // 예약 시간 설정
		reservation.setEmailAddress(reservationInput.getEmailAddress()); // 예약자 이메일 주소 설정

		// 예약 정보를 데이터베이스에 삽입
		reservationRepository.insert(reservation);

		// 생성된 예약 정보를 반환
		return reservation;
	}
}
