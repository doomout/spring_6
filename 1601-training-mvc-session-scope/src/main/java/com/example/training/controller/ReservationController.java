package com.example.training.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.entity.Training;
import com.example.training.exception.CapacityOverException;
import com.example.training.input.ReservationInput;
import com.example.training.service.ReservationService;
import com.example.training.service.TrainingService;

@Controller // 스프링 MVC의 컨트롤러로 등록
@RequestMapping("/reservation") // "/reservation" 경로에 대한 요청을 처리
public class ReservationController {

    private final ReservationService reservationService; // 예약 관련 서비스

    private final TrainingService trainingService; // 교육 과정 관련 서비스

    private final ReservationSession reservationSession; // 세션 스코프에서 예약 정보를 관리하는 객체

    // 생성자를 통해 서비스 및 세션 객체를 주입받음
    public ReservationController(ReservationService reservationService, TrainingService trainingService, ReservationSession reservationSession) {
        this.reservationService = reservationService;
        this.trainingService = trainingService;
        this.reservationSession = reservationSession;
    }

    /**
     * 예약 입력 폼을 표시하는 메서드
     * @param trainingId 교육 과정 ID
     * @param model 뷰에 전달할 데이터 저장 객체
     * @return 예약 입력 폼 페이지
     */
    @GetMapping("/display-form")
    public String displayForm(@RequestParam String trainingId, Model model) {
        // 새로운 예약 입력 객체 생성
        ReservationInput reservationInput = new ReservationInput();
        reservationInput.setTrainingId(trainingId); // 교육 과정 ID 설정
        reservationInput.setStudentTypeCode("EMPLOYEE"); // 기본적으로 "EMPLOYEE"로 설정

        model.addAttribute("reservationInput", reservationInput); // 모델에 추가하여 뷰에서 사용 가능하게 함
        List<StudentType> studentTypes = reservationService.findAllStudentType(); // 모든 수강자 유형 조회
        model.addAttribute("studentTypeList", studentTypes); // 모델에 추가하여 뷰에서 선택 가능하게 함

        return "reservation/reservationForm"; // 예약 입력 폼 페이지 반환
    }

    /**
     * 예약 입력값을 검증하는 메서드
     * @param reservationInput 사용자가 입력한 예약 정보
     * @param bindingResult 입력값 검증 결과
     * @param model 뷰에 전달할 데이터 저장 객체
     * @return 유효성 검사 결과에 따라 예약 확인 페이지 또는 다시 입력 폼 페이지 반환
     */
    @PostMapping("/validate-input")
    public String validateInput(@Validated ReservationInput reservationInput, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) { // 입력값 검증에서 오류가 발생한 경우
            List<StudentType> studentTypeList = reservationService.findAllStudentType(); // 모든 수강자 유형 조회
            model.addAttribute("studentTypeList", studentTypeList); // 다시 선택할 수 있도록 모델에 추가
            return "reservation/reservationForm"; // 입력 폼 페이지로 돌아감
        }

        StudentType studentType = reservationService.findStudentTypeByCode(reservationInput.getStudentTypeCode()); // 입력된 수강자 유형 조회
        model.addAttribute("studentType", studentType); // 모델에 추가하여 뷰에서 사용 가능하게 함

        Training training = trainingService.findById(reservationInput.getTrainingId()); // 입력된 교육 과정 정보 조회
        model.addAttribute("training", training); // 모델에 추가하여 뷰에서 사용 가능하게 함

        reservationSession.setReservationInput(reservationInput); // 세션 스코프에 예약 정보 저장
        return "reservation/reservationConfirmation"; // 예약 확인 페이지 반환
    }

    /**
     * 예약 입력값을 수정하는 메서드
     * @param model 뷰에 전달할 데이터 저장 객체
     * @return 예약 입력 폼 페이지 반환
     */
    @PostMapping(value = "/reserve", params = "correct")
    public String correctInput(Model model) {
        model.addAttribute("reservationInput", reservationSession.getReservationInput()); // 세션에서 예약 정보 가져와 모델에 추가
        List<StudentType> studentTypeList = reservationService.findAllStudentType(); // 모든 수강자 유형 조회
        model.addAttribute("studentTypeList", studentTypeList); // 다시 선택할 수 있도록 모델에 추가
        return "reservation/reservationForm"; // 예약 입력 폼 페이지 반환
    }

    /**
     * 예약을 확정하는 메서드
     * @param model 뷰에 전달할 데이터 저장 객체
     * @return 예약 완료 페이지 반환
     */
    @PostMapping(value = "/reserve", params = "reserve")
    public String reserve(Model model) {
        ReservationInput reservationInput = reservationSession.getReservationInput(); // 세션에서 예약 정보 가져옴
        Reservation reservation = reservationService.reserve(reservationInput); // 예약 확정
        model.addAttribute("reservation", reservation); // 예약 정보를 모델에 추가하여 뷰에서 사용 가능하게 함

        reservationSession.clearData(); // 세션 데이터 삭제 (예약 완료 후 세션 정보 초기화)

        return "reservation/reservationCompletion"; // 예약 완료 페이지 반환
    }

    /**
     * 수용 인원을 초과했을 때 예외 처리
     * @return 수용 인원 초과 페이지 반환
     */
    @ExceptionHandler(CapacityOverException.class)
    public String displayCapacityOverPage() {
        return "reservation/capacityOver"; // 수용 인원 초과 안내 페이지 반환
    }

}
