package com.gym.tracker.controller;

import com.gym.tracker.model.Member;
import com.gym.tracker.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Member endpoints
 *
 * POST    /api/members                      → Create (sign up)
 * GET     /api/members                      → Read All
 * GET     /api/members/{id}                 → Read One
 * GET     /api/members/expiring-this-month  → Expiring subscriptions
 * GET     /api/members/expired              → Already expired
 * GET     /api/members/status/{status}      → Filter by status
 * GET     /api/members/plan/{planId}        → Members on a plan
 * GET     /api/members/trainer/{trainerId}  → Members assigned to trainer
 * PUT     /api/members/{id}                 → Update contact/personal info
 * PATCH   /api/members/{id}/upgrade-plan    → Upgrade membership
 * DELETE  /api/members/{id}                 → Cancel / remove
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // ── CREATE ──────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Member> createMember(
            @Valid @RequestBody Member member) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.createMember(member));
    }

    // ── READ ALL ────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // ── READ ONE ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    // ── READ: Expiring this month ────────────────────────────────
    @GetMapping("/expiring-this-month")
    public ResponseEntity<List<Member>> getExpiringThisMonth() {
        return ResponseEntity.ok(memberService.getExpiringThisMonth());
    }

    // ── READ: Already expired ────────────────────────────────────
    @GetMapping("/expired")
    public ResponseEntity<List<Member>> getExpiredMembers() {
        return ResponseEntity.ok(memberService.getExpiredMembers());
    }

    // ── READ: By Status ─────────────────────────────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Member>> getMembersByStatus(
            @PathVariable Member.MemberStatus status) {
        return ResponseEntity.ok(memberService.getMembersByStatus(status));
    }

    // ── READ: By Plan (Many-to-One reverse) ─────────────────────
    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<Member>> getMembersByPlan(@PathVariable String planId) {
        return ResponseEntity.ok(memberService.getMembersByPlan(planId));
    }

    // ── READ: By Trainer ────────────────────────────────────────
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<Member>> getMembersByTrainer(@PathVariable String trainerId) {
        return ResponseEntity.ok(memberService.getMembersByTrainer(trainerId));
    }

    // ── UPDATE: Personal info ────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @PathVariable String id,
            @Valid @RequestBody Member member) {
        return ResponseEntity.ok(memberService.updateMember(id, member));
    }

    // ── UPDATE: Upgrade plan ─────────────────────────────────────
    @PatchMapping("/{id}/upgrade-plan")
    public ResponseEntity<?> upgradePlan(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String newPlanId = body.get("planId");
        if (newPlanId == null || newPlanId.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Request body must contain 'planId'."));
        }
        return ResponseEntity.ok(memberService.upgradePlan(id, newPlanId));
    }

    // ── DELETE ──────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMember(@PathVariable String id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(Map.of("message", "Member profile deleted successfully."));
    }
}
