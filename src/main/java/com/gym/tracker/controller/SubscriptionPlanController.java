package com.gym.tracker.controller;

import com.gym.tracker.model.SubscriptionPlan;
import com.gym.tracker.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Subscription Plan endpoints
 *
 * POST   /api/plans               → Create
 * GET    /api/plans               → Read All
 * GET    /api/plans/{id}          → Read One
 * GET    /api/plans/{id}/members  → List member IDs on this plan (One-to-Many)
 * PUT    /api/plans/{id}          → Update
 * DELETE /api/plans/{id}          → Delete
 */
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    // ── CREATE ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<SubscriptionPlan> createPlan(
            @Valid @RequestBody SubscriptionPlan plan) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(planService.createPlan(plan));
    }

    // ── READ ALL ────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<SubscriptionPlan>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    // ── READ ONE ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> getPlanById(@PathVariable String id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    // ── READ MEMBERS (One-to-Many relationship view) ─────────────
    @GetMapping("/{id}/members")
    public ResponseEntity<Map<String, Object>> getMembersOfPlan(@PathVariable String id) {
        List<String> memberIds = planService.getMemberIdsByPlan(id);
        return ResponseEntity.ok(Map.of(
                "planId",    id,
                "count",     memberIds.size(),
                "memberIds", memberIds
        ));
    }

    // ── UPDATE ──────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlan> updatePlan(
            @PathVariable String id,
            @Valid @RequestBody SubscriptionPlan plan) {
        return ResponseEntity.ok(planService.updatePlan(id, plan));
    }

    // ── DELETE ──────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
        return ResponseEntity.ok(Map.of("message", "Subscription plan deleted successfully."));
    }
}
