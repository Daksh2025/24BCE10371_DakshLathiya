package com.gym.tracker.controller;

import com.gym.tracker.model.Department;
import com.gym.tracker.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Department endpoints
 *
 * POST   /api/departments           → Create
 * GET    /api/departments           → Read All
 * GET    /api/departments/active    → Read Active Only
 * GET    /api/departments/{id}      → Read One
 * PUT    /api/departments/{id}      → Update
 * DELETE /api/departments/{id}      → Delete
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // ── CREATE ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Department> createDepartment(
            @Valid @RequestBody Department department) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(department));
    }

    // ── READ ALL ────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // ── READ ACTIVE ─────────────────────────────────────────────
    @GetMapping("/active")
    public ResponseEntity<List<Department>> getActiveDepartments() {
        return ResponseEntity.ok(departmentService.getActiveDepartments());
    }

    // ── READ ONE ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable String id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    // ── UPDATE ──────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable String id,
            @Valid @RequestBody Department department) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    // ── DELETE ──────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully."));
    }
}
