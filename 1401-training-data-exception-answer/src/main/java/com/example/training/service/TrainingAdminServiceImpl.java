
package com.example.training.service;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.training.entity.Training;
import com.example.training.input.TrainingAdminInput;
import com.example.training.repository.TrainingRepository;

@Service
@Transactional
public class TrainingAdminServiceImpl implements TrainingAdminService {

	private final TrainingRepository trainingRepository;

	public TrainingAdminServiceImpl(TrainingRepository trainingRepository) {
		this.trainingRepository = trainingRepository;
	}

	@Override
	public List<Training> findAll() {
		return trainingRepository.selectAll();
	}

	@Override
	public Training findById(String trainingId) {
		return trainingRepository.selectById(trainingId);
	}

	@Override
	public void update(TrainingAdminInput trainingAdminInput) {
		Training training = new Training();
		training.setId(trainingAdminInput.getId());
		training.setTitle(trainingAdminInput.getTitle());
		training.setStartDateTime(trainingAdminInput.getStartDateTime());
		training.setEndDateTime(trainingAdminInput.getEndDateTime());
		training.setReserved(trainingAdminInput.getReserved());
		training.setCapacity(trainingAdminInput.getCapacity());
		boolean result = trainingRepository.update(training);
		if (!result) {
			throw new OptimisticLockingFailureException("이미 삭제됐을 가능성이 있습니다. id=" + training.getId());
		}
	}

	@Override
	public void delete(String id) {
		trainingRepository.delete(id);
	}

	@Override
	public Training register(TrainingAdminInput trainingAdminInput) {
		Training training = new Training();
		training.setId(trainingAdminInput.getId());
		training.setTitle(trainingAdminInput.getTitle());
		training.setStartDateTime(trainingAdminInput.getStartDateTime());
		training.setEndDateTime(trainingAdminInput.getEndDateTime());
		training.setReserved(trainingAdminInput.getReserved());
		training.setCapacity(trainingAdminInput.getCapacity());
		trainingRepository.insert(training);
		return training;
	}
}
