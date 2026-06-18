package com.gym.tracker.service;

import com.gym.tracker.exception.DuplicateResourceException;
import com.gym.tracker.exception.ResourceNotFoundException;
import com.gym.tracker.model.Trainer;
import com.gym.tracker.repository.DepartmentRepository;
import com.gym.tracker.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository    trainerRepository;
    private final DepartmentRepository departmentRepository;

    // ── CREATE ──────────────────────────────────────────────────
    public Trainer createTrainer(Trainer trainer) {
        if (trainerRepository.existsByEmail(trainer.getEmail())) {
            throw new DuplicateResourceException(
                    "Trainer with email '" + trainer.getEmail() + "' already exists.");
        }
        validateDepartment(trainer.getDepartmentId());

        Trainer saved = trainerRepository.save(trainer);
        log.info("Created trainer: {} [{}]", saved.getName(), saved.getId());
        return saved;
    }

    // ── READ: All ───────────────────────────────────────────────
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    // ── READ: By ID ─────────────────────────────────────────────
    public Trainer getTrainerById(String id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer", id));
    }

    // ── READ: By Department (Many-to-One reverse lookup) ────────
    public List<Trainer> getTrainersByDepartment(String departmentId) {
        // Ensure department exists first
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", departmentId);
        }
        return trainerRepository.findByDepartmentId(departmentId);
    }

    // ── READ: Available trainers ────────────────────────────────
    public List<Trainer> getAvailableTrainers() {
        return trainerRepository.findByAvailable(true);
    }

    // ── READ: By Specialization ─────────────────────────────────
    public List<Trainer> getTrainersBySpecialization(String specialization) {
        return trainerRepository.findBySpecialization(specialization);
    }

    // ── UPDATE ──────────────────────────────────────────────────
    public Trainer updateTrainer(String id, Trainer updated) {
        Trainer existing = getTrainerById(id);

        if (!existing.getEmail().equals(updated.getEmail())
                && trainerRepository.existsByEmail(updated.getEmail())) {
            throw new DuplicateResourceException(
                    "Email '" + updated.getEmail() + "' is already in use.");
        }

        validateDepartment(updated.getDepartmentId());

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setSpecialization(updated.getSpecialization());
        existing.setCertification(updated.getCertification());
        existing.setAvailable(updated.isAvailable());
        existing.setDepartmentId(updated.getDepartmentId());

        log.info("Updated trainer: {}", id);
        return trainerRepository.save(existing);
    }

    // ── DELETE ──────────────────────────────────────────────────
    public void deleteTrainer(String id) {
        Trainer trainer = getTrainerById(id);
        trainerRepository.delete(trainer);
        log.info("Deleted trainer: {} [{}]", trainer.getName(), id);
    }

    // ── Private helper ──────────────────────────────────────────
    private void validateDepartment(String departmentId) {
        if (departmentId != null && !departmentId.isBlank()) {
            if (!departmentRepository.existsById(departmentId)) {
                throw new ResourceNotFoundException("Department", departmentId);
            }
        }
    }
}
