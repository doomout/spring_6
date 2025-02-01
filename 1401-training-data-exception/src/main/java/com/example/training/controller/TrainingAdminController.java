package com.example.training.controller;

import java.util.List;

import com.example.training.input.TrainingAdminInput;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.training.entity.Training;
import com.example.training.service.TrainingAdminService;

@Controller
@RequestMapping("/admin/training")
public class TrainingAdminController {

	private final TrainingAdminService trainingAdminService;

	public TrainingAdminController(TrainingAdminService trainingAdminService) {
		this.trainingAdminService = trainingAdminService;
	}

	@GetMapping("/display-list")
	public String displayList(Model model) {
		List<Training> trainings = trainingAdminService.findAll();
		model.addAttribute("trainingList", trainings);
		return "admin/training/trainingList";
	}

	@GetMapping("/display-registration-form")
	public String displayRegistrationForm(Model model) {
		TrainingAdminInput trainingAdminInput = new TrainingAdminInput();
		model.addAttribute("trainingAdminInput", trainingAdminInput);
		return "admin/training/registrationForm";
	}

	@PostMapping(value = "/validate-registration-input")
	public String validateRegistrationInput(@Validated TrainingAdminInput trainingAdminInput, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "admin/training/registrationForm";
		}
		return "admin/training/registrationConfirmation";
	}

	@PostMapping(value = "/register", params = "correct")
	public String correctRegistrationInput(@Validated TrainingAdminInput trainingAdminInput) {
		return "admin/training/registrationForm";
	}

	@PostMapping(value = "/register", params = "register")
	public String register(@Validated TrainingAdminInput trainingAdminInput, Model model) {
		//키가 중복된 경우, 원래 입력 화면으로 되돌려서 다른 키를 입력하도록 유도
		try {
			trainingAdminService.register(trainingAdminInput);
		} catch (DuplicateKeyException ex) {
			model.addAttribute("duplicateError", "키가 중복되었습니다. 다른 키를 입력하세요.");
			return "admin/training/registrationForm";
		}
        model.addAttribute("trainingId", trainingAdminInput.getId());
        return "admin/training/registrationCompletion";
	}


}
