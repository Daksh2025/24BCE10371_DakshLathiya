package com.gym.tracker.service;

import com.gym.tracker.exception.BusinessRuleException;
import com.gym.tracker.exception.DuplicateResourceException;
import com.gym.tracker.exception.ResourceNotFoundException;
import com.gym.tracker.model.Department;
import com.gym.tracker.repository.DepartmentRepository;
import com.gym.tracker.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final TrainerRepository    trainerRepository;

    // ── CREATE ──────────────────────────────────────────────────
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateResourceException(
                    "Department with name '" + department.getName() + "' already exists.");
        }
        Department saved = departmentRepository.save(department);
        log.info("Created department: {} [{}]", saved.getName(), saved.getId());
        return saved;
    }

    // ── READ: All ───────────────────────────────────────────────
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // ── READ: Active only ───────────────────────────────────────
    public List<Department> getActiveDepartments() {
        return departmentRepository.findByActive(true);
    }

    // ── READ: By ID ─────────────────────────────────────────────
    public Department getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    }

    // ── UPDATE ──────────────────────────────────────────────────
    public Department updateDepartment(String id, Department updated) {
        Department existing = getDepartmentById(id);

        // If name is being changed, ensure uniqueness
        if (!existing.getName().equals(updated.getName())
                && departmentRepository.existsByName(updated.getName())) {
            throw new DuplicateResourceException(
                    "Department with name '" + updated.getName() + "' already exists.");
        }

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setLocation(updated.getLocation());
        existing.setContactEmail(updated.getContactEmail());
        existing.setActive(updated.isActive());

        log.info("Updated department: {}", id);
        return departmentRepository.save(existing);
    }

    // ── DELETE ──────────────────────────────────────────────────
    public void deleteDepartment(String id) {
        Department department = getDepartmentById(id);

        // Guard: cannot delete if trainers are still assigned
        int trainerCount = trainerRepository.findByDepartmentId(id).size();
        if (trainerCount > 0) {
            throw new BusinessRuleException(
                    "Cannot delete department '" + department.getName() + "'. "
                    + trainerCount + " trainer(s) are still assigned to it. "
                    + "Re-assign or remove them first.");
        }

        departmentRepository.delete(department);
        log.info("Deleted department: {} [{}]", department.getName(), id);
    }
}
