package com.gym.tracker.repository;

import com.gym.tracker.model.Trainer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends MongoRepository<Trainer, String> {

    Optional<Trainer> findByEmail(String email);

    boolean existsByEmail(String email);

    // Many-to-One reverse: get all trainers in a department
    List<Trainer> findByDepartmentId(String departmentId);

    List<Trainer> findByAvailable(boolean available);

    List<Trainer> findBySpecialization(String specialization);
}
