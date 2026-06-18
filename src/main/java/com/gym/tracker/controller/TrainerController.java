package com.gym.tracker.controller;

import com.gym.tracker.model.Trainer;
import com.gym.tracker.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Trainer endpoints
 *
 * POST   /api/trainers                          → Create
 * GET    /api/trainers                          → Read All
 * GET    /api/trainers/available                → Read Available
 * GET    /api/trainers/{id}                     → Read One
 * GET    /api/trainers/department/{departmentId}→ By Department (Many-to-One)
 * GET    /api/trainers/specialization/{spec}    → By Specialization
 * PUT    /api/trainers/{id}                     → Update
 * DELETE /api/trainers/{id}                     → Delete
 */
@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    // ── CREATE ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Trainer> createTrainer(
            @Valid @RequestBody Trainer trainer) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trainerService.createTrainer(trainer));
    }

    // ── READ ALL ────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return ResponseEntity.ok(trainerService.getAllTrainers());
    }

    // ── READ AVAILABLE ──────────────────────────────────────────
    @GetMapping("/available")
    public ResponseEntity<List<Trainer>> getAvailableTrainers() {
        return ResponseEntity.ok(trainerService.getAvailableTrainers());
    }

    // ── READ ONE ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable String id) {
        return ResponseEntity.ok(trainerService.getTrainerById(id));
    }

    // ── READ BY DEPARTMENT (Many-to-One reverse lookup) ─────────
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Trainer>> getTrainersByDepartment(
            @PathVariable String departmentId) {
        return ResponseEntity.ok(trainerService.getTrainersByDepartment(departmentId));
    }

    // ── READ BY SPECIALIZATION ──────────────────────────────────
    @GetMapping("/specialization/{spec}")
    public ResponseEntity<List<Trainer>> getTrainersBySpecialization(
            @PathVariable String spec) {
        return ResponseEntity.ok(trainerService.getTrainersBySpecialization(spec));
    }

    // ── UPDATE ──────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Trainer> updateTrainer(
            @PathVariable String id,
            @Valid @RequestBody Trainer trainer) {
        return ResponseEntity.ok(trainerService.updateTrainer(id, trainer));
    }

    // ── DELETE ──────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTrainer(@PathVariable String id) {
        trainerService.deleteTrainer(id);
        return ResponseEntity.ok(Map.of("message", "Trainer deleted successfully."));
    }
}
